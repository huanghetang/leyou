package com.leyou.page.client;

import com.leyou.http.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhoumo
 * @datetime 2018/7/28 20:15
 * @desc
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
