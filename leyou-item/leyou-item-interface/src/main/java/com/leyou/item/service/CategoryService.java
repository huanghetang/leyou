package com.leyou.item.service;

import com.leyou.pojo.Category;
import com.leyou.pojo.CategoryBrand;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/19 20:32
 * @desc
 */
public interface CategoryService {
    //查询分类列表
    List<Category> queryItemListById(Long id);

    //添加商品分类
    void addCategory(Category category);


    //检查当前节点是不是叶子节点
    public boolean checkIsLeafNode(Long id);

    //删除商品分类
    void editCategoryById(Long id, String name);

    //修改商品分类的名称
    void deleteCategoryById(Long id);

    List<Category> getCategoryListByBrandId(Long brandId);

    List<CategoryBrand> getCategoryBrandById(Long brandId);

    /**
     * 根据id查询分类信息
     */
    List<Category> getCategoryListByIds(Long...id);

    Category queryCategoryById(Long cid);

    List<Category> getAllByCid3(Long cid3);
}
