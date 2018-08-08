package com.leyou.controller;

import com.leyou.pojo.Goods;
import com.leyou.pojo.PageResult;
import com.leyou.pojo.SearchRequest;
import com.leyou.search.service.GoodsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhoumo
 * @datetime 2018/7/27 15:17
 * @desc   商品搜索
 */
@RestController
public class SearchController {
    @Autowired
    private GoodsIndexService goodsIndexService;



    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> queryGoodsByKey(@RequestBody SearchRequest search){
        PageResult<Goods> pageResult = goodsIndexService.queryGoodsByKey(search);
        if(pageResult==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pageResult);
    }
}
