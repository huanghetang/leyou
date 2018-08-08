package com.leyou;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecificationClient;
import com.leyou.pojo.*;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.GoodsIndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/26 17:23
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApp.class)
public class TestFeign {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsIndexService goodsIndexService;
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private BrandClient brandClient;


    @Test
    public void test1() {
        PageResult<SPU> page = goodsClient.getSPUListByPage("手机", true, 1l, 10l);
        Long total = page.getTotal();
        List<SPU> items = page.getItems();
        for (SPU item : items) {
            System.out.println("item = " + item);
        }
    }

    //创建索引库并建立索引库类型和对象的映射关系
    @Test
    public void test2() {
        boolean index = elasticsearchTemplate.createIndex(Goods.class);
        boolean b = elasticsearchTemplate.putMapping(Goods.class);
        System.out.println(index + "  " + b);
    }

    @Test
    public void test21() {
        List<SpecParam> specList = specificationClient.getSpecificationParamListByGid(null, 76l, null, null, true, true);
        specList.stream().forEach(System.out::println);
        System.out.println(specList.size());
    }

    @Test
    public void test22() {
        List<Category> categoryList = categoryClient.queryCategoryByIds(Arrays.asList(74l, 75l, 76l));
        for (Category category : categoryList) {
            System.out.println("category = " + category);
        }
    }

    //测试索引对象
    @Test
    public void test3() {
        PageResult<SPU> page = goodsClient.getSPUListByPage("手机", true, 1l, 5l);

        if (page != null && !CollectionUtils.isEmpty(page.getItems())) {
            Long total = page.getTotal();
            System.out.println("total = " + total);
            List<SPU> items = page.getItems();
            SPU spu = items.get(0);
            System.out.println("spu = " + spu);
            Goods goods = goodsIndexService.CreateGoodsBySpu(spu);
            System.out.println("goods = " + goods);
        }
    }

    //添加商品索引库
    @Test
    public void importGoodsIndex2ES() {
        long page = 1;
        long rows = 100;
        long count = 0;
        PageResult<SPU> pageResult = null;
        do {
            //分页查询SPU商品每次100条
            pageResult = goodsClient.getSPUListByPage(null, true, page, rows);
            if (pageResult == null || CollectionUtils.isEmpty(pageResult.getItems())) {
                return;
            }
            List<SPU> spus = pageResult.getItems();
            ArrayList<Goods> goodsList = new ArrayList<>();
            for (SPU spu : spus) {
                Goods goods = goodsIndexService.CreateGoodsBySpu(spu);
                goodsList.add(goods);
            }
            //添加商品索引
            goodsRepository.saveAll(goodsList);
            page++;
            count = spus.size();
        } while (count == 100);

    }


    @Test
    public void test44() {

        List<Brand> brandList = brandClient.queryBrandListByIds(Arrays.asList(7174l, 7203l));
        for (Brand brand : brandList) {
            System.out.println("brand = " + brand);
        }

    }

    @Test
    public void test444() {
        List<SpecParam> specParams = specificationClient.
                getSpecificationParamListByGid(null, 76l, null, null, null, true);
        for (SpecParam specParam : specParams) {
            System.out.println("specParam = " + specParam);
        }


    }

    @Test
    public void test555() {
        List<Category> allByCid3 = categoryClient.getAllByCid3(76l);
        allByCid3.forEach(System.out::println);
    }


}
