package com.leyou;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.pojo.Category;
import com.leyou.pojo.CategoryBrand;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/7/20 12:07
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemServiceApp.class)
public class Test {
    @Autowired
    private CategoryMapper categoryMapper;

    @org.junit.Test
    public void test1(){
        Category category = new Category();
        category.setId(null);
        category.setName("景甜");
        category.setIsParent(false);
        category.setParentId(0l);
        category.setSort(1);
        int i = categoryMapper.insertSelective(category);
        System.out.println("i = " + i);
    }

    @org.junit.Test
    public void test2(){
        List<Map<String, String>> map = categoryMapper.queryCategoryAndBrand(325402l);
        System.out.println("map = " + map);
    }

    @org.junit.Test
    public void test3(){
        List<CategoryBrand> categoryBrands = categoryMapper.queryCategoryBrand(325402l);
        System.out.println("categoryBrands = " + categoryBrands);
    }
}
