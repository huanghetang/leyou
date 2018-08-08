package com.leyou.item.service;

import com.leyou.pojo.Brand;
import com.leyou.pojo.PageResult;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/21 14:46
 * @desc  商标接口
 */
public interface BrandService {
   /**
    * 商标分页查询
    * @param key
    * @param page
    * @param rows
    * @param sortBy
    * @param desc
    * @return
    */
   PageResult<Brand> queryBrandListByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

   /**
    * 添加商标
    * @param brand
    */
   void addBrand(Brand brand,List<String> ids);

    /**
     * 根据商标id修改商标
     * @param brand
     * @param cids
     */
    void editBrandByBrandId(Brand brand, List<Long> cids);

    /**
     * 根据商标id删除商标
     * @param brandId
     */
    void deleteBrandById(Long brandId);
    /**
     * 根据商标id查询商标
     * @param brandId
     */
    Brand queryBrandById(Long brandId);

    List<Brand> queryBrandListByIds(List<Long> cids);
}
