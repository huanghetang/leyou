package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.service.BrandService;
import com.leyou.pojo.Brand;
import com.leyou.pojo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/21 14:48
 * @desc
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    //storage客户端
    @Autowired
    private FastFileStorageClient storageClient;
    //带缩略图的storage客户端
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    /**
     * 商标分页查询
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandListByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //设置分页属性,起始页,每页显示数
        PageHelper.startPage(page, rows);
        //关键字过滤条件
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key))
            example.createCriteria().andLike("name", "%" + key + "%");
        //排序条件,排序规则
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = desc ? (sortBy + " asc") : (sortBy + " desc");
            example.setOrderByClause(orderByClause);
        }
        //查询结果
        List<Brand> brands = brandMapper.selectByExample(example);
        //创建分页对象
        PageInfo pageInfo = new PageInfo(brands);

        PageResult<Brand> pageResult = new PageResult<>();
        //当前页数据
        pageResult.setItems(brands);
        //总条数
        pageResult.setTotal(pageInfo.getTotal());
        //返回
        return pageResult;
    }

    /**
     * 添加商标
     *
     * @param brand
     * @param ids
     */
    @Transactional
    @Override
    public void addBrand(Brand brand, List<String> ids) {
        //添加商标
        brand.setId(null);
        brandMapper.insert(brand);
        //添加中间表数据(tb_category_brand)
        for (String categoryId : ids) {
            brandMapper.insertCategoryBrand(categoryId, brand.getId());
        }
    }


    @Transactional
    @Override
    public void editBrandByBrandId(Brand brand, List<Long> cids) {
        //修改商标
        brandMapper.updateByPrimaryKeySelective(brand);
        //先删除后添加
        //删除这个商标相关的所有商品分类
        brandMapper.deleteCategoryBrand(brand.getId());
        //添加中间表数据
        for (Long cid : cids) {
            brandMapper.insertCategoryBrand(cid+"",brand.getId());
        }
    }

    @Transactional
    @Override
    public void deleteBrandById(Long brandId) {
        try{
            //删除中间表数据
            brandMapper.deleteCategoryBrand(brandId);
            //删除fastDFS图片 TODO 发送消息通知 leyou-upload模块删除图片
            Brand brand = brandMapper.selectByPrimaryKey(brandId);
            String imageUrl = brand.getImage();
            if(StringUtils.isNotBlank(imageUrl)){
                storageClient.deleteFile(StringUtils.substringAfter(imageUrl,"http://image.leyou.com/"));
                System.out.println("fastDFS服务器删除了一张图片: = " + imageUrl);
            }
            //删除商标表数据
            brandMapper.delete(brand);
        }catch(Exception e){
            throw new RuntimeException("删除商标异常",e);
        }

    }

    @Override
    public Brand queryBrandById(Long brandId) {
        return brandMapper.selectByPrimaryKey(brandId);
    }

    @Override
    public List<Brand> queryBrandListByIds(List<Long> cids) {
       return brandMapper.selectByIds(StringUtils.join(cids,","));
    }

    public static void main(String[] args) {
        String str = "http://image.leyou.com/group1/M00/00/00/wKgZhVtTS4KAY55LAACenmGVJ5o413.jpg";
        String s = StringUtils.substringAfter(str, "http://image.leyou.com/");
        System.out.println("s = " + s);
    }
}
