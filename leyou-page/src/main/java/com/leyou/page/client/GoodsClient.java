package com.leyou.page.client;

import com.leyou.http.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhoumo
 * @datetime 2018/7/30 16:09
 * @desc
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
