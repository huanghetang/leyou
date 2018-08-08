package com.leyou.item.mapper;

import com.leyou.pojo.Category;
import com.leyou.pojo.CategoryBrand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/7/19 20:36
 * @desc
 */
public interface CategoryMapper extends Mapper<Category>,SelectByIdListMapper<Category,Long> {

    /**
     * 根据商标id查询商品数据
     * @param brandId
     * @return
     */
    @Select("select * from tb_category_brand where brand_id = #{brandId}")
    List<Category> queryCategoryListByBrandId(@Param("brandId") Long brandId);

    @Select("select * from tb_category_brand where brand_id = #{brandId}")
    List<Map<String,String>> queryCategoryAndBrand(@Param("brandId") Long brandId);

    @Select({"select * from tb_category_brand where brand_id = #{brandId}"})

    List<CategoryBrand> queryCategoryBrand(@Param("brandId") Long brandId);
}
