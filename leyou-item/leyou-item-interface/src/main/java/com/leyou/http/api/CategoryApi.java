package com.leyou.http.api;

import com.leyou.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/26 18:44
 * @desc
 */
@RequestMapping("category")
public interface CategoryApi {
    /**
     * 根据品牌id查询商品分类
     * oldBrand.id
     */
    @GetMapping("bid/{brandId}")
    List<Category> getCategoryListByBrandId(@PathVariable(value = "brandId") Long brandId);


    /**
     * 根据商品分类id查询商品分类
     *
     * @param cids
     * @return
     */
    @GetMapping("cid")
    List<Category> queryCategoryByIds(@RequestParam(value = "cids") List<Long> cids);

    /**
     * 根据cid3,查询1-3级的商品分类信息
     *
     * @param cid3
     * @return
     */
    @GetMapping("all/level")
    List<Category> getAllByCid3(@RequestParam(value = "cid3") Long cid3);
}
