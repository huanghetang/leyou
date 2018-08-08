package com.leyou.page.client;

import com.leyou.http.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhoumo
 * @datetime 2018/7/26 20:27
 * @desc    商品分类restful远程调用feign客户端
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
