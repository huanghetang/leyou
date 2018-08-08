package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author zhoumo
 * @datetime 2018/7/19 21:03
 * @desc
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.leyou.item.mapper")
public class ItemServiceApp{
    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApp.class,args);
    }
}
