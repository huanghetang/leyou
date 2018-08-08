package com.leyou.item.controller;


import com.leyou.item.service.CategoryService;
import com.leyou.pojo.Category;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author zhoumo
 * @datetime 2018/7/19 20:22
 * @desc 商品分类接口
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据商品分类父亲id查询商品分类子孙
     *
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryItemListById(@RequestParam("pid") Long pid) {
        List<Category> itemList = categoryService.queryItemListById(pid);
        if (itemList == null && itemList.size() == 0) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(itemList);
    }


    /**
     * 添加商品分类
     */
    @PostMapping("addCategory")
    public ResponseEntity addCategory(@RequestBody Category category) {
        System.out.println("category = " + category);
        categoryService.addCategory(category);
        return ResponseEntity.ok("添加成功");
    }

    /**
     * 修改节点
     */
    @PutMapping("editCategoryById")
    public ResponseEntity editCategoryById(@RequestBody(required = false) Category category,
                                           @RequestBody(required = false) Long id,
                                           @RequestParam(required = false) String name) {
        categoryService.editCategoryById(id, name);
        return ResponseEntity.ok("修改成功");
    }

    /**
     * 删除任意节点
     *
     * @param id
     * @return
     */
    @DeleteMapping("deleteCategoryById")
    public ResponseEntity deleteCategoryById(@RequestParam(value = "id") Long id) {
        System.out.println("id = " + id);
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("删除成功");
    }

    /**
     * 根据品牌id查询商品分类
     * oldBrand.id
     */
    @GetMapping("bid/{brandId}")
    public ResponseEntity<List<Category>> getCategoryListByBrandId(@PathVariable(value = "brandId") Long brandId){
        List<Category> categoryList =  categoryService.getCategoryListByBrandId(brandId);
        return ResponseEntity.ok(categoryList);
    }

    /**
     * 根据商品分类id查询商品分类
     * @param cids
     * @return
     */
    @GetMapping("cid")
    public ResponseEntity<List<Category>> queryCategoryById(@RequestParam(value = "cids") List<Long> cids){
        List<Category> categoryList = categoryService.getCategoryListByIds(cids.toArray(new Long[0]));
        if(categoryList==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(categoryList);
    }


    /**
     * 根据cid3,查询1-3级的商品分类信息
     * @param cid3
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> getAllByCid3(@RequestParam(value = "cid3") Long cid3){
        List<Category> categoryList = categoryService.getAllByCid3(cid3);
        if(categoryList==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(categoryList);
    }

    public static void main(String[] args) {
       List<Long> cids = Arrays.asList(1l, 2l, 3l);
        Long[] longs = cids.toArray(new Long[0]);
        for (Long aLong : longs) {
            System.out.println("aLong = " + aLong);
        }
    }
}
