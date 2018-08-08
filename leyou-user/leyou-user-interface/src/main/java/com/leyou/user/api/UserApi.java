package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhoumo
 * @datetime 2018/8/3 17:28
 * @desc
 */

public interface UserApi {

    /**
     * 根据用户名和密码查询用户
     * @return
     */
    @GetMapping("query")
    User query(@RequestParam("username") String username,@RequestParam("password") String password);


}
