package com.leyou.pojo;

import java.util.List;
import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/7/28 19:30
 * @desc
 */
public class SearchResult extends  PageResult<Goods> {

    //商品分类过滤条件
    private List<Category> categoryList;
    //商品品牌过滤条件
    private List<Brand> brandList;
    //规格参数过滤条件
    private List<Map<String,Object>> specMapList;

    public List<Map<String, Object>> getSpecMapList() {
        return specMapList;
    }

    public void setSpecMapList(List<Map<String, Object>> specMapList) {
        this.specMapList = specMapList;
    }

    public SearchResult(Long total, List<Goods> items, Integer totalPages, List<Category> categoryList, List<Brand> brandList) {
        super(total, items,totalPages);
        this.categoryList = categoryList;
        this.brandList = brandList;
    }
    public SearchResult(){}

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }
}
