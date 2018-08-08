package com.leyou.cart.interceptor;

import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.utils.JwtUtils;
import com.leyou.sms.utils.CookieUtils2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;

/**
 * @author zhoumo
 * @datetime 2018/8/5 16:02
 * @desc   用户登陆状态拦截器,校验到用户登陆后将用户信息保存到线程共享容器中
 */
public class UserInterceptor implements HandlerInterceptor{
    private static final ThreadLocal<UserInfo> threadLocal = new ThreadLocal<UserInfo>();
    private String cookieName;
    private PublicKey publicKey;
    private static final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);
    public UserInterceptor(PublicKey publicKey,String cookieName){
        this.publicKey = publicKey;
        this.cookieName = cookieName;
    }
    //后置处理
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //使用完毕清除线程共享容器中的数据
        threadLocal.remove();
    }

    //前置处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //使用公钥解析token
        String token = CookieUtils2.getCookieValue(request, cookieName);
        //解析失败后拦截
        if(StringUtils.isBlank(token)){
            logger.error("用户校验失败,获取token失败");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, publicKey);
        if (userInfo==null){
            logger.error("用户校验失败");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        //解析成功将user信息放入到线程共享变量中并放行
        threadLocal.set(userInfo);
        return true;

    }

    public static UserInfo getUserInfo(){
        return threadLocal.get();
    }
}
