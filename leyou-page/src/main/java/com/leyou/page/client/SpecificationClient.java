package com.leyou.page.client;

import com.leyou.http.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhoumo
 * @datetime 2018/7/26 21:29
 * @desc  规格参数服务远程调用客户端
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
