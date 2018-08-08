package com.leyou.item.controller;

import com.leyou.item.service.impl.GoodsService;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/22 19:31
 * @desc 商品接口
 */
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;


    /**
     * 根据spuId查询spu
     */
    @GetMapping("spu/id")
    public ResponseEntity<SPU> querySPUById(@RequestParam("id") Long id){
        SPU spu = goodsService.querySPUById(id);
        if(spu ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spu);
    }

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
    public ResponseEntity<PageResult<SPU>> getSPUListByPage(@RequestParam(required = false, value = "key") String key,
                                           @RequestParam(required = false, value = "saleable") Boolean saleable,
                                           @RequestParam(value = "page", defaultValue = "1") Long page,
                                           @RequestParam(value = "rows", defaultValue = "5") Long rows) {
        PageResult<SPU> pageResult = goodsService.querySPUListByPage(key, saleable, page, rows);
        if (pageResult == null) {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 根据商品分类ID查询商品商标
     */
    @GetMapping("brand/cid/{categoryId}")
    public ResponseEntity<List<Brand>> getBrandByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        List<Brand> brandList = goodsService.getBrandByCategoryId(categoryId);
        if (brandList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(brandList);
    }

    /**
     * 添加商品
     */
    @PostMapping("goods")
    public ResponseEntity<Void> addGoods(@RequestBody SPU spuGoods) {
        goodsService.addGoods(spuGoods);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 根据spuid查询spuDetail详情
     */
    @GetMapping("spu/detail/{sid}")
    public ResponseEntity<SpuDetail> getSpuDetailBySid(@PathVariable("sid") Long sid) {
       SpuDetail spuDetail = goodsService.getSpuDetailBySid(sid);
        if(spuDetail ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spuDetail);
    }
    /**
     *根据spuid查询sku详情
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<SKU>> getSKUListBySid(@RequestParam("id") Long sid){
       List<SKU> skuList = goodsService.getSKUListBySid(sid);
        if(skuList ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(skuList);
    }

    /**
     * 修改商品
     */
    @PutMapping("goods")
    public ResponseEntity<Void> editGoods(@RequestBody SPU spuGoods) {
        if(spuGoods==null || spuGoods.getId()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        goodsService.editGoods(spuGoods);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("goods/{sid}")
    public ResponseEntity<Void> deleteGoodsByCid(@PathVariable(value = "sid") Long sid){
        goodsService.deleteGoodsByCid(sid);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }
    /**
     * 上下架商品
     */
    @PutMapping("goods/saleable/{flag}")
    public ResponseEntity<Void> unsaleableGoodsByCid(@PathVariable("flag") Long flag,@RequestParam(value = "sid") Long sid){
        goodsService.unsaleableGoodsByCid(flag,sid);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据skuId查询sku对应的库存
     */
    @GetMapping("goods/sku/stock")
    public ResponseEntity<Stock> querySotckBySkuId(@RequestParam("skuId") Long skuId){
        Stock stock = goodsService.querySotckBySkuId(skuId);
        if(stock ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(stock);
    }

}
