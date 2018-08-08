package com.leyou.test;

import com.leyou.page.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;


/**
 * @author zhoumo
 * @datetime 2018/7/31 8:54
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestReadProperties {
    @Autowired
    private SpringTemplateEngine springTemplateEngine;
    @Autowired
    private ItemService itemService;

    @Value("${ly.thymeleaf.destPath}")
    private  String DESTPATH;

    @Test
    public void test1(){
        System.out.println("DESTPATH = " + DESTPATH);
    }

    @Test
    public void test2(){
        PrintWriter writer = null;
        Long spuId = 13l;
        try {
            Map<String, Object> map = itemService.loadData(spuId);
            //创建上下文数据
            Context context = new org.thymeleaf.context.Context();
            context.setVariables(map);
            //创建流
            writer = new PrintWriter(new File("E:\\nginx-1.12.2\\html\\item",spuId+".html"));
            //读取thymeleaf模板和上下文数据,生成静态文件
            springTemplateEngine.process("item", context, writer);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }
}
