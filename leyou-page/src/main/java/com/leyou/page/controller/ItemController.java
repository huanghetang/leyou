package com.leyou.page.controller;

import com.leyou.page.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/7/29 22:32
 * @desc
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 查看详细,使用thymeleaf模板引擎渲染页面返回给用户,并生成静态页面保存到磁盘
     * @param spuId
     * @param model
     * @return
     */
    @GetMapping("item/{spuId}.html")
    public String querySPUDetail(@PathVariable("spuId") Long spuId,Model model){
        //加载数据
        Map<String,Object> map = itemService.loadData(spuId);
        //放入模型
        model.addAllAttributes(map);
        //异步生成静态页面保存到磁盘
//        itemService.asyncCreateHtml(spuId);
        //返回视图
        return "item";
    }
}
