package com.leyou.mq;

import com.leyou.page.service.ItemService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhoumo
 * @datetime 2018/8/1 19:06
 * @desc 监听商品微服务的商品添加修改删除消息
 */
@Component
public class ItemListener {
    @Autowired
    private ItemService itemService;

    /**
     * 监听商品添加和修改的消息,重新创建Html静态页面
     *
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "ly.page.insertAndUpdateQueue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = {"ly.item.insert", "ly.item.update"}))
    public void addItemHandler(Long spuId) {
        if (spuId != null) {
            itemService.createHtml(spuId);
        }
    }

    /**
     * 监听商品添加和修改的消息,删除对应的Html静态页面
     *
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "ly.page.deleteQueue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "ly.item.delete"))
    public void deleteItemHandler(Long spuId) {
        if (spuId != null) {
            itemService.deleteHtml(spuId);
        }
    }

}

