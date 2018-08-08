package com.leyou.cart.config;

import com.leyou.cart.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhoumo
 * @datetime 2018/8/5 16:36
 * @desc  注册拦截器
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class RegisterUserInterceptor implements WebMvcConfigurer {
    @Autowired
    private JwtProperties props;

    @Bean
    public UserInterceptor userInterceptor(){
        UserInterceptor userInterceptor = new UserInterceptor(props.getPublicKey(),props.getCookieName());
        return userInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor()).addPathPatterns("/**");
    }
}
