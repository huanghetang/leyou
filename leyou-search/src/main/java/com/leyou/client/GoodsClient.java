package com.leyou.client;

import com.leyou.http.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhoumo
 * @datetime 2018/7/26 18:53
 * @desc
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
