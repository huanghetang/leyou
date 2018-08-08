package com.leyou.item.mapper;

import com.leyou.pojo.Brand;
import com.leyou.pojo.SPU;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/22 20:54
 * @desc
 */
public interface SPUMapper extends Mapper<SPU> {


    /**
     * 根据商品分类id查询商标
     */
    @Select("select b.* from tb_brand b left JOIN tb_category_brand cb on b.id = cb.brand_id where cb.category_id = #{cid}")
    List<Brand> getBrandByCategoryId(@Param("cid") Long cid);
}
