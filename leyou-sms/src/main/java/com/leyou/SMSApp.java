package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhoumo
 * @datetime 2018/8/2 6:59
 * @desc  发送短信服务启动类
 */
@SpringBootApplication
public class SMSApp {

    public static void main(String[] args) {
        SpringApplication.run(SMSApp.class,args);
    }

}
