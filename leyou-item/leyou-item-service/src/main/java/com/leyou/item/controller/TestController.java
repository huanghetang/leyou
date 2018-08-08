package com.leyou.item.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoumo
 * @datetime 2018/7/15 17:50
 * @desc
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "hello,乐游";
    }
}
