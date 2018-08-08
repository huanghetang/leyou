package com.leyou;

import org.junit.Test;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhoumo
 * @datetime 2018/8/1 17:07
 * @desc
 */
@Component
public class TestListener {

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "ly.item.testListener", durable = "false"),
                                exchange = @Exchange(value = "ly.item.exchange", durable = "false",
                                ignoreDeclarationExceptions = "true",type = ExchangeTypes.DIRECT),
                                key = "ly.item.insert"))
    public void test(String msg){
        System.out.println("123123======================================================="  + "=======================================================");
        System.out.println("msg = " + msg);

    }
}
