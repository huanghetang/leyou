package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import com.leyou.pojo.Category;
import com.leyou.pojo.CategoryBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/19 20:33
 * @desc
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandService brandService;

    @Override
    public List<Category> queryItemListById(Long id) {
        Category item = new Category();
        item.setParentId(id);
        List<Category> itemList = categoryMapper.select(item);
        return itemList;
    }

    /**
     * @param category
     */
    @Transactional //加事务
    @Override
    public void addCategory(Category category) {
        //设置当前节点的属性
        category.setId(null);
        category.setIsParent(false);
        //插入当前节点
        categoryMapper.insertSelective(category);
        //获取当前节点的父节点id
        Long fatherNodeId = category.getParentId();
        //修改当前节点的父节点的isParent为true,(查询为false才需要修改 但每次都需要查询)
        Category fatherNode = new Category();
        fatherNode.setId(fatherNodeId);
        fatherNode.setIsParent(true);
        categoryMapper.updateByPrimaryKeySelective(fatherNode);
    }


    /**
     * 查询当前节点是不是叶子节点
     *
     * @param id
     * @return 是叶子节点返回true, 不是就返回false
     */
    @Override
    public boolean checkIsLeafNode(Long id) {
        //查询子节点个数,有子节点就返回false
        Category node = new Category();
        node.setParentId(id);
        int count = categoryMapper.selectCount(node);
        return count > 0 ? false : true;
    }

    /**
     * 根据id修改节点名字
     *
     * @param id
     * @param name
     */
    @Override
    public void editCategoryById(Long id, String name) {
        //修改当前节点的名称
        Category node = new Category();
        node.setId(id);
        node.setName(name);
        categoryMapper.updateByPrimaryKeySelective(node);
    }

    /**
     * 删除任意节点
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteCategoryById(Long id) {
        //1 判断当前节点是不是叶子节点
        boolean isLeafNode = checkIsLeafNode(id);

        //2 当前节点为叶子节点时
        if (isLeafNode) {
            //查询当前节点的父节点id值
            Category thisNode = categoryMapper.selectByPrimaryKey(id);
            Long fatherNodeId = thisNode.getParentId();
            //检查父节点的子节点个数
            Category fatherNode = new Category();
            fatherNode.setId(fatherNodeId);
            int sonNodeNumber = categoryMapper.selectCount(fatherNode);
            //为1时 修改父节点的isParent属性为false
            if(sonNodeNumber==1){
                fatherNode.setIsParent(false);
                categoryMapper.updateByPrimaryKeySelective(fatherNode);
            }
            //删除当前节点
            categoryMapper.deleteByPrimaryKey(id);
        } else {
            //3 当前节点为非叶子节点时
            //查询该节点下的子节点
            Example example = new Example(Category.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId",id);
            List<Category> sonNodeList = categoryMapper.selectByExample(example);
            //递归
            for (Category thisNode : sonNodeList) {
                deleteCategoryById(thisNode.getId());
            }
            //递归结束后(把子孙节点全部删除后)再次删除当前节点
            deleteCategoryById(id);
        }

    }

    /**
     * 根据商标id查询商品分类数据
     * @param brandId
     * @return
     */
    @Override
    public List<Category> getCategoryListByBrandId(Long brandId) {
        //查询中间表
        List<CategoryBrand> categoryBrandList =  this.getCategoryBrandById(brandId);
        //创建分类信息
        ArrayList<Category> categoryList = new ArrayList<>();
        for (CategoryBrand categoryBrand:categoryBrandList){
            //查询分类信息
            Long categoryId = categoryBrand.getCategoryId();
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            //添加分类
            categoryList.add(category);
        }
        return categoryList;
    }

    @Override
    public List<CategoryBrand> getCategoryBrandById(Long brandId) {
        List<CategoryBrand> categoryList = categoryMapper.queryCategoryBrand(brandId);
        return categoryList;
    }

    @Override
    public List<Category> getCategoryListByIds(Long... id) {
        List<Long> idList = Arrays.asList(id);
        List<Category> categoryList = categoryMapper.selectByIdList(idList);
        return categoryList;
    }

    @Override
    public Category queryCategoryById(Long cid) {
        return categoryMapper.selectByPrimaryKey(cid);
    }

    @Override
    public List<Category> getAllByCid3(Long cid3) {
        Category category3 = categoryMapper.selectByPrimaryKey(cid3);
        Category category2 = categoryMapper.selectByPrimaryKey(category3.getParentId());
        Category category1 = categoryMapper.selectByPrimaryKey(category2.getParentId());
        return  Arrays.asList(category1,category2,category3);
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(5, 4, 7);
        list.stream().forEach(System.out::println);
    }


}
