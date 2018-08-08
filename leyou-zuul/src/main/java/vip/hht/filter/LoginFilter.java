package vip.hht.filter;

import com.leyou.auth.common.utils.JwtUtils;
import com.leyou.sms.utils.CookieUtils2;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import vip.hht.config.FilterProperties;
import vip.hht.config.JwtProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/8/4 16:47
 * @desc  登陆校验拦截器(权限控制)
 */
@Component
@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties props;
    @Autowired
    private FilterProperties filterProps;
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        //过滤器类型
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER +1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String requestURI = request.getRequestURI();
        //对哪些请求进行过滤,return false 表示不过滤,return true表示过滤
        return !isAllowedPath(requestURI);
    }

    private boolean isAllowedPath(String requestURI) {
        if (StringUtils.isBlank(requestURI)){
            return false;
        }
        List<String> allowPaths = filterProps.getAllowpath();
        if(allowPaths==null){
            return false;
        }
        for (String allowPath : allowPaths) {
            if (requestURI.startsWith(allowPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        //处理逻辑
        //获取request对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //获取token值
        String token = CookieUtils2.getCookieValue(request, props.getCookieName());
        try {
            //使用公钥对token解密,解码成功表示用户校验成功
            JwtUtils.getInfoFromToken(token,props.getPublicKey());
        } catch (Exception e) {
           logger.error("非法访问,未登录!访问主机:",request.getRemoteHost(),e);
            //解密失败,拦截此次请求
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
