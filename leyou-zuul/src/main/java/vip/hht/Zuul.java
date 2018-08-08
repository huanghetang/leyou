package vip.hht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author zhoumo
 * @datetime 2018/7/15 16:34
 * @desc
 */
@SpringBootApplication
@EnableDiscoveryClient//开启注册中心客户端功能
@EnableZuulProxy//开启网关服务
public class Zuul {
    public static void main(String[] args) {
        SpringApplication.run(Zuul.class,args);
    }
}
