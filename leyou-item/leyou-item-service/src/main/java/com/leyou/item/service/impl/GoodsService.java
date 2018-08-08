package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.mapper.*;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import com.leyou.pojo.*;
import com.leyou.sms.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhoumo
 * @datetime 2018/7/22 20:52
 * @desc
 */
@Service
public class GoodsService {
    Logger logger = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private SPUMapper spuMapper;
    @Autowired
    private SKUMapper skuMapper;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpecParamMapper specParamMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 分页查询
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    public PageResult<SPU> querySPUListByPage(String key, Boolean saleable, Long page, Long rows) {
        //初始化分页信息
        PageHelper.startPage(page.intValue(), rows.intValue());
        //设置分页条件
        Example example = new Example(SPU.class);
        Example.Criteria criteria = example.createCriteria();
        //关键字搜索
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        //是否上架 1上架,0下架
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        //没有删除数据
        criteria.andEqualTo("valid", true);
        //查询数据
        List<SPU> spuList = spuMapper.selectByExample(example);
        //设置商品分类和品牌显示数据
        if (!CollectionUtils.isEmpty(spuList)) {
            setShowProperties(spuList);
        }

        PageInfo<SPU> pageInfo = new PageInfo<>(spuList);
        //创建分页信息对象
        //设置分页对象属性
        //返回分页信息
        return new PageResult<SPU>(pageInfo.getTotal(), spuList);
    }

    private void setShowProperties(List<SPU> spuList) {
        //遍历每一个SPU对象,并设置 分类属性和品牌属性
        for (SPU spu : spuList) {
            Long categoryId1 = spu.getCid1();
            Long categoryId2 = spu.getCid2();
            Long categoryId3 = spu.getCid3();
//            List<Category> categoryList = categoryService.getCategoryListByIds(categoryId1, categoryId2, categoryId3);
//            spu.setCname(categoryList.get(0).getName()+"/"+categoryList.get(1).getName()+"/"+categoryList.get(2).getName());
            List<String> nameList = categoryService.getCategoryListByIds(categoryId1, categoryId2, categoryId3)
                    .stream()
                    .map(c -> c.getName())
                    .collect(Collectors.toList());
            spu.setCname(StringUtils.join(nameList, "/"));
            Brand brand = brandService.queryBrandById(spu.getBrandId());
            spu.setBname(brand.getName());
        }

    }


    /**
     * 根据商品分类id查询商品分类对应的商标
     *
     * @param categoryId
     * @return
     */
    public List<Brand> getBrandByCategoryId(Long categoryId) {
        return spuMapper.getBrandByCategoryId(categoryId);
    }

    /**
     * 根据商品分类id查询商品分类对应的规格参数
     *
     * @param cid
     * @return
     */
    public List<SpecParam> querySpecParamListByCid(Long cid) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        List<SpecParam> specParamList = specParamMapper.select(specParam);
        return specParamList;
    }

    /**
     * 添加商品
     *
     * @param spu
     */
    @Transactional
    public void addGoods(SPU spu) {
        try {
            //添加SPU
            spu.setSaleable(true);
            spu.setValid(true);
            spu.setCreateTime(new Date());
            spu.setLastUpdateTime(spu.getCreateTime());

            int count = spuMapper.insert(spu);
            if (count == 0) {
                logger.error("添加SPU失败!SPU:" + JsonUtils.serialize(spu));
                throw new RuntimeException("添加SPU失败!");
            }
            //添加spuDetail
            SpuDetail spuDetail = spu.getSpuDetail();
            spuDetail.setSpuId(spu.getId());
            count = spuDetailMapper.insert(spuDetail);
            if (count == 0) {
                logger.error("添加spuDetail失败!spuDetail:" + JsonUtils.serialize(spuDetail));
                throw new RuntimeException("添加spuDetail失败!");
            }
            //添加SKU
            List<SKU> skus = spu.getSkus();
            InsertSKUListAndStockList(spu, skus);
            //异步发送mq到交换机
            amqpTemplate.convertAndSend("ly.item.goods.insert",spu.getId());
        } catch (Exception e) {
            logger.error("添加商品失败!" + JsonUtils.serialize(spu));
            throw new RuntimeException("添加商品失败!", e);
        }

    }

    /**
     * 插入一条SPU对应的多条sku和库存
     *
     * @param spu
     * @param skus
     */
    private void InsertSKUListAndStockList(SPU spu, List<SKU> skus) {
        int count;
        ArrayList<Stock> stockList = new ArrayList<Stock>();
        for (SKU sku : skus) {
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            //是否有效，0无效，1有效
            sku.setEnable(true);
            count = skuMapper.insert(sku);
            if (count == 0) {
                logger.error("添加sku失败!sku:" + JsonUtils.serialize(sku));
                throw new RuntimeException("添加sku失败!");
            }
            //设置库存字段
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
        }
        //批量添加库存
        count = stockMapper.insertList(stockList);
        if (count != skus.size()) {
            logger.error("添加stock失败!stock:" + JsonUtils.serialize(stockList));
            throw new RuntimeException("添加stock失败!");
        }
    }

    /**
     * 根据spuid 查询spuDetail
     *
     * @param sid
     * @return
     */
    public SpuDetail getSpuDetailBySid(Long sid) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(sid);
        return spuDetail;
    }

    /**
     * 根据spuid 查询所有的sku
     *
     * @param sid
     * @return
     */
    public List<SKU> getSKUListBySid(Long sid) {
        //根据spu的外键查询sku的信息
        SKU sku = new SKU();
        sku.setSpuId(sid);
        List<SKU> skuList = skuMapper.select(sku);
        if (!CollectionUtils.isEmpty(skuList)) {
            for (SKU sku1 : skuList) {
                Long sku1Id = sku1.getId();
                //查询sku的库存信息,并给sku对象设置值
                Stock stock = stockMapper.selectByPrimaryKey(sku1Id);
                sku1.setStock(stock.getStock());
            }
        }
        return skuList;
    }

    /**
     * 修改商品
     *
     * @param spu
     */
    @Transactional
    public void editGoods(SPU spu) {
        try {
            //设置spu属性并更新
            spu.setCreateTime(null);
            //是否上架，0下架，1上架
            spu.setSaleable(null);
            //是否有效，0已删除，1有效
            spu.setValid(null);
            spu.setLastUpdateTime(new Date());
            int count = spuMapper.updateByPrimaryKeySelective(spu);
            if (count == 0) {
                logger.error("修改spu失败!spu:" + JsonUtils.serialize(spu));
                throw new RuntimeException("修改spu失败!");
            }
            //修改spuDetail
            count = spuDetailMapper.updateByPrimaryKey(spu.getSpuDetail());
            if (count == 0) {
                logger.error("修改spuDetail失败!spuDetail:" + JsonUtils.serialize(spu.getSpuDetail()));
                throw new RuntimeException("修改spuDetail失败!");
            }
            //修改sku,先删除,再添加
            //查询出原SKU数据
            SKU sku = new SKU();
            sku.setSpuId(spu.getId());
            List<SKU> skuList = skuMapper.select(sku);
            if (!CollectionUtils.isEmpty(skuList)) {
                List<Long> ids = skuList.stream().map(SKU::getId).collect(Collectors.toList());
                //批量删除原SKU数据
                count = skuMapper.deleteByIdList(ids);
                if (count != ids.size()) {
                    logger.error("删除sku失败!sku对应的主键分别是:" + JsonUtils.serialize(ids));
                    throw new RuntimeException("删除skus失败!");
                }
                count = stockMapper.deleteByIdList(ids);
                if (count != ids.size()) {
                    logger.error("删除stock失败!stock对应的主键分别是:" + JsonUtils.serialize(ids));
                    throw new RuntimeException("删除stock失败!");
                }
            }
            //保存sku和库存stoke
            List<SKU> skus = spu.getSkus();
            InsertSKUListAndStockList(spu, skus);
            //异步发送mq到交换机
            sendMQ("ly.item.update",spu.getId());
        } catch (Exception e) {
            throw new RuntimeException("修改商品失败", e);
        }
    }

    /**
     * 删除商品
     *
     * @param sid
     */
    public void deleteGoodsByCid(Long sid) {
        try {
            //将valid 设置成false 是否有效，0已删除，1有效
            SPU spu = new SPU();
            spu.setId(sid);
            spu.setValid(false);
            int count = spuMapper.updateByPrimaryKeySelective(spu);
            if (count ==0) {
                throw new RuntimeException("删除商品失败!id:" + sid);
            }
            //异步发送mq到交换机
            sendMQ("ly.item.delete",spu.getId());
        } catch (Exception e) {
            logger.error("删除商品失败!id:" + sid);
            throw new RuntimeException("删除商品失败!id:" + sid, e);
        }

    }

    /**
     * 上下架商品
     * @param flag 0表示要下架,1表示要上架
     * @param sid
     */
    public void unsaleableGoodsByCid(Long flag,Long sid) {
        try {

            SPU spu = new SPU();
            spu.setId(sid);
            //saleable 是否上架，0下架，1上架
            if(flag.intValue()==0){
                spu.setSaleable(false);
            }else{
                spu.setSaleable(true);
            }
            int count = spuMapper.updateByPrimaryKeySelective(spu);
            if (count ==0) {
                throw new RuntimeException("商品下架失败!id:" + sid);
            }
            //TODO
            //amqpTemplate.convertAndSend("ly.item.goods.delete",spu.getId());
        } catch (Exception e) {
            logger.error("商品下架失败!id:" + sid);
            throw new RuntimeException("商品下架失败!id:" + sid,e);
        }
    }

    public SPU querySPUById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    public Stock querySotckBySkuId(Long skuId) {
       return stockMapper.selectByPrimaryKey(skuId);
    }

    private void sendMQ(String routingKey,Long spuId){
        try {
            amqpTemplate.convertAndSend(routingKey, spuId);
            System.out.println("发送了一条消息routingKey:"+routingKey+"\tspuId:"+spuId);
        }catch (Exception e){
            logger.error("RabbitMQ消息发送失败",routingKey,spuId,e);
        }
    }
}
