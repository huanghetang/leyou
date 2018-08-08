package com.leyou.item.mapper;

import com.leyou.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhoumo
 * @datetime 2018/7/21 14:48
 * @desc
 */
public interface BrandMapper extends Mapper<Brand>,IdsMapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES(#{categoryId}, #{brandId});")
    void insertCategoryBrand(@Param("categoryId") String categoryId, @Param("brandId") Long brandId);


    /**
     * 删除商品分类-商标中间表
     */
    @Delete("delete from tb_category_brand where brand_id = #{brandId} ")
    void deleteCategoryBrand(@Param("brandId") Long brandId);
}
