package com.leyou.cart.service;

import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.sms.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhoumo
 * @datetime 2018/8/5 17:32
 * @desc
 */
@Service
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${ly.redis.cartKey}")
    private String CART_KEY_PREFIX;

    /**
     * 添加购物车到缓存
     *
     * @param cart
     */
    public void addCart(Cart cart) {
        //从线程共享变量中获取用户信息
        UserInfo userInfo = UserInterceptor.getUserInfo();
        Long userId = userInfo.getId();
        String cacheKey = CART_KEY_PREFIX + ":" + userId;
        String hashKey = cart.getSkuId().toString();
        Integer num = cart.getNum();
        //查询redis中有无该提交的购物车
        BoundHashOperations<String, Object, Object> hashOperation =
                                    stringRedisTemplate.boundHashOps(cacheKey);
        //如果购物车中有这条商品
        cart.setUserId(userId);
        if (hashOperation.hasKey(hashKey)) {
            //取出购物车
            cart = JsonUtils.parse(hashOperation.get(hashKey).toString(), Cart.class);
            //重新设置购物车数量
            cart.setNum(cart.getNum() + num);
        }
        //添加到redis
        hashOperation.put(hashKey, JsonUtils.serialize(cart));
    }

    /**
     * 合并购物车
     *
     * @param carts
     */
    public void mergeCart(List<Cart> carts) {
        //遍历localStorage中的购物车
        UserInfo userInfo = UserInterceptor.getUserInfo();
        BoundHashOperations<String, Object, Object> boundHashOps
                = stringRedisTemplate.boundHashOps(CART_KEY_PREFIX+":"+userInfo.getId());
        for (Cart cart : carts) {
            Long skuId = cart.getSkuId();
            //存在修改数量
            if (boundHashOps.hasKey(skuId.toString())) {
                int cacheNum = JsonUtils.parse(boundHashOps.get(skuId.toString()).toString(), Cart.class).getNum();
                cart.setNum(cacheNum + cart.getNum());
            }
            boundHashOps.put(skuId.toString(), JsonUtils.serialize(cart));

        }
    }



    /**
     * 查询购物车
     * @return
     */
    public List<Cart> queryCart() {
        //从线程共享变量中获取用户信息
        UserInfo userInfo = UserInterceptor.getUserInfo();
        BoundHashOperations<String, Object, Object> boundHashOps
                    = stringRedisTemplate.boundHashOps(CART_KEY_PREFIX+":"+userInfo.getId().toString());
        //从redis中查询该用户名下的商品
        List<Object> carts = boundHashOps.values();
        return carts.stream().map(o-> JsonUtils.parse(o.toString(),Cart.class)).collect(Collectors.toList());
    }

    /**
     * 修改商品
     */
    public void editCart(Long skuId,Integer num) {

        //查询用户
        UserInfo userInfo = UserInterceptor.getUserInfo();
        //绑定redis 哈希
        BoundHashOperations<String, Object, Object> boundHashOps =
                stringRedisTemplate.boundHashOps(CART_KEY_PREFIX+":"+userInfo.getId().toString());
        //修改商品
        //查询商品
        String json = boundHashOps.get(skuId.toString()).toString();
        Cart cart = JsonUtils.parse(json, Cart.class);
        cart.setNum(num);
        boundHashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(Long id) {
        //查询用户
        UserInfo userInfo = UserInterceptor.getUserInfo();
        //绑定redis 哈希
        BoundHashOperations<String, Object, Object> boundHashOps =
                stringRedisTemplate.boundHashOps(CART_KEY_PREFIX+":"+userInfo.getId().toString());
        //删除商品
        boundHashOps.delete(id.toString());
    }

}
