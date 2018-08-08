package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhoumo
 * @datetime 2018/8/3 18:21
 * @desc
 */
@FeignClient("user-service")
public interface UserQueryClient extends UserApi {
}
