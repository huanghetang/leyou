package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author zhoumo
 * @datetime 2018/7/26 16:38
 * @desc
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableElasticsearchRepositories
public class SearchApp {
    public static void main(String[] args) {
        SpringApplication.run(SearchApp.class,args);
    }
}
