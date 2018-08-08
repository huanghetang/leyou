package com.leyou;

/**
 * @author zhoumo
 * @datetime 2018/8/1 16:13
 * @desc
 */

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.pojo.Category;
import com.leyou.pojo.CategoryBrand;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/7/20 12:07
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemServiceApp.class)
public class AMQPTest {

@Autowired
private AmqpTemplate amqpTemplate;

    @org.junit.Test
    public void test1(){
        amqpTemplate.convertAndSend("ly.item.insert","测试insert1231231");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

