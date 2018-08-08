package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zhoumo
 * @datetime 2018/7/21 17:43
 * @desc
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UploadServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(UploadServiceApp.class,args);
    }
}
