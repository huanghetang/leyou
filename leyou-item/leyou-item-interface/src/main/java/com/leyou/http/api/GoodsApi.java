package com.leyou.http.api;

import com.leyou.pojo.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/26 18:46
 * @desc
 */
public interface GoodsApi {

    /**
     * 分页查询商品SPU
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SPU> getSPUListByPage(@RequestParam(required = false, value = "key") String key,
                                     @RequestParam(required = false, value = "saleable") Boolean saleable,
                                     @RequestParam(value = "page", defaultValue = "1") Long page,
                                     @RequestParam(value = "rows", defaultValue = "5") Long rows);


    /**
     * 根据spuid查询sku详情
     */
    @GetMapping("sku/list")
    List<SKU> getSKUListBySid(@RequestParam("id") Long sid);

    /**
     * 根据spuid查询spuDetail详情
     */
    @GetMapping("spu/detail/{sid}")
    SpuDetail getSpuDetailBySid(@PathVariable("sid") Long sid);

    /**
     * 根据spuId查询spu
     */
    @GetMapping("spu/id")
    SPU querySPUById(@RequestParam("id") Long id);

    /**
     * 根据skuId查询sku对应的库存
     */
    @GetMapping("goods/sku/stock")
    Stock querySotckBySkuId(@RequestParam("skuId") Long skuId);


}
