package com.leyou.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhoumo
 * @datetime 2018/8/3 21:21
 * @desca
 */
@FeignClient("user-service")
public interface UserQueryClient extends UserApi{
}
