package com.leyou.page.service;

import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import com.leyou.pojo.*;
import com.leyou.sms.utils.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/7/30 15:34
 * @desc 商品服务
 */
@Service
public class ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    @Value("${ly.thymeleaf.destPath}")
    private String DESTPATH;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    public static void main(String[] args) {
//        File file = new File("14.html");
        File file2 = new File("E:\\nginx-1.12.2\\html\\item\\13.html");

//        boolean b = file.renameTo(file2);
//        System.out.println("b = " + b);
//        boolean delete = file.delete();
        System.out.println(file2.exists());
    }


    /**
     * 查询商品详情
     *
     * @param spuId
     * @return
     */
    public Map<String, Object> loadData(Long spuId) {
        HashMap<String, Object> map = new HashMap<>();
        //根据spuId查询spu
        SPU spu = goodsClient.querySPUById(spuId);
        map.put("spu", spu);
        //获取1-3级分类id查询1-3级分类 ,只需要id和name
        List<Category> categories = getCategories(spu);
        map.put("categories", categories);
        //获取到brandId 查询品牌
        Brand brand = brandClient.queryBrandListByIds(Arrays.asList(spu.getBrandId())).get(0);
        map.put("brand", brand);
        //根据spuId查询spuDetail
        SpuDetail detail = goodsClient.getSpuDetailBySid(spuId);
        map.put("detail", detail);
        //根据spuId查询sku集合
        List<SKU> skus = goodsClient.getSKUListBySid(spuId);
        for (SKU sku : skus) {
            ///根据sku查询stock
            Stock stock = goodsClient.querySotckBySkuId(sku.getId());
            //库存赋值
            sku.setStock(stock.getStock());
        }
        map.put("skus", skus);
        //根据spuId中的3级分类查询到多个规格参数组List<Group>
        //根据每一个规格参数组查询到每个组对应的多个规格参数,并放到规格参数中 list<param>
        List<SpecGroup> specGroups = specificationClient.querySpecGroupListWithSpecParamByCid(spu.getCid3());
        map.put("specGroups", specGroups);
        //把所有数据放入到map中返回
        return map;
    }

    private List<Category> getCategories(SPU spu) {
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        Category category1 = new Category();
        category1.setId(spu.getCid1());
        category1.setName(categories.get(0).getName());

        Category category2 = new Category();
        category2.setId(spu.getCid2());
        category2.setName(categories.get(1).getName());

        Category category3 = new Category();
        category3.setId(spu.getCid3());
        category3.setName(categories.get(2).getName());
        return Arrays.asList(category1, category2, category3);
    }

    /**
     * 点击详情页时根据spuid和查询出的数据使用thymeleaf生成静态html文件
     */
    public void createHtml(Long spuId) {
        //创建 临时文件,备份文件,目标文件的文件对象
        File destFile = createNewFile(spuId);
        File tempFile = new File(spuId + ".html");
        File backFile = new File(spuId + "_back.html");
        //创建流 先保存到临时文件
        try (FileWriter writer = new FileWriter(tempFile)) {
            //创建thymeleaf上下文对象并加载数据
            Context context = new Context();
            context.setVariables(loadData(spuId));
            //调用模板引擎process方法生成html
            springTemplateEngine.process("item", context, writer);
            //如果该id.html文件存则先备份该文件
            if (destFile.exists()) {
                //剪切
                destFile.renameTo(backFile);
            }
            //把临时文件复制到目标文件
            FileCopyUtils.copy(tempFile, destFile);
            logger.info("生成静态商品详情页成功:spuId:" + spuId);
            //成功后销毁备份文件,文件不存在也不会抛异常
            backFile.delete();
        } catch (Exception e) {
            //失败后将备份文件还原
            backFile.renameTo(destFile);
            logger.error("生成静态商品详情页失败:spuId:" + spuId, e);
            throw new RuntimeException(e);
        } finally {
            //销毁临时文件
            if(tempFile.exists()){
                tempFile.delete();
            }
        }
    }

    private File createNewFile(Long spuId) {
        if(spuId==null){
            return null;
        }
        File file = new File(DESTPATH);
        if (!file.exists()){
            file.mkdirs();
        }
        return new File(file,spuId+".html");
    }

    /**
     * 异步生成html
     */
    public void asyncCreateHtml(Long spuId){
        //使用线程池执行thymeleaf生成静态页面的操作
        ThreadPoolUtils.execute(()->createHtml(spuId));
    }


    public void deleteHtml(Long spuId) {
        File file = createNewFile(spuId);
        file.deleteOnExit();
        logger.info("删除了一个静态页面spuId:{}",spuId);
    }
}
