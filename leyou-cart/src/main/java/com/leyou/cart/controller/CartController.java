package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.pojo.LocalCart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/8/5 17:18
 * @desc
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 登陆状态用户添加购物车
     *
     * @param cart 购物车数据模型
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 查询购物车
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCart(){
        List<Cart> carts = cartService.queryCart();
        if(CollectionUtils.isEmpty(carts)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(carts);
    }


    /**
     * 合并购物车
     */
    @PostMapping("merge")
    public ResponseEntity<Void> mergeCart(@RequestBody List<Cart> cartList) {
        if(cartList==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        cartService.mergeCart(cartList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    public ResponseEntity<Void> editCart(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num) {
        cartService.editCart(skuId,num);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id") Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
