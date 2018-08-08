package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhoumo
 * @datetime 2018/7/15 16:15
 * @desc
 */
@SpringBootApplication
@EnableEurekaServer
public class Register {
    public static void main(String[] args) {
        SpringApplication.run(Register.class,args);
    }
}
