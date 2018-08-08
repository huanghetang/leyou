package com.leyou.order.service.api;

import com.leyou.http.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: HuYi.Zhang
 * @create: 2018-05-02 19:48
 **/
//@FeignClient(value = "api-gateway", path = "/api/item")
@FeignClient(value = "zuul", path = "/api/item")
public interface GoodsService extends GoodsApi {
}
