package com.leyou.auth.controller;

import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.utils.JwtUtils;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.sms.utils.CookieUtils2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoumo
 * @datetime 2018/8/3 17:00
 * @desc 用户授权接口
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties props;

    /**
     * 用户登陆并给用户授权
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         HttpServletRequest request, HttpServletResponse response) {
        //登陆授权
        String token = authService.accredit(username, password);
        if (StringUtils.isBlank(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //保存到cookie中
        CookieUtils2.newBuilder(request, response).httpOnly()
                .cookieMaxAge(props.getCookieMaxAge())
                .build(props.getCookieName(), token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * 根据客户端携带的token信息进行登陆校验,验证成功后刷新用户的token有效时间
     *
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token,
                                           HttpServletRequest request, HttpServletResponse response) {
        //获取token中的user信息
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, props.getPublicKey());
            if (userInfo == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            //刷新token的有效期
            //重新生成jwt
            String newToken = JwtUtils.generateToken(userInfo, props.getPrivateKey(), props.getExpire());
            //重新写入cookie
            CookieUtils2.newBuilder(request, response).cookieMaxAge(props.getCookieMaxAge())
                    .httpOnly().build(props.getCookieName(), newToken);
            //返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 用户退出
     * @param token
     * @param request
     * @param response
     * @return
     */
    @DeleteMapping("logout")
    public ResponseEntity<Void> logout(@CookieValue("LY_TOKEN") String token,HttpServletRequest request,HttpServletResponse response){
        //从cookie中取出token信息删除
        CookieUtils2.newBuilder(request,response).cookieMaxAge(1).build(props.getCookieName(),"");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
