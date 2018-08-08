package com.leyou.cart.pojo;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/8/6 12:14
 * @desc
 */
public class LocalCart {
    private List<Cart> carts;

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
