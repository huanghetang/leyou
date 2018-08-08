package com.leyou.mq;

import com.leyou.search.service.GoodsIndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhoumo
 * @datetime 2018/8/1 19:42
 * @desc 监听商品服务中商品的添加修改和删除并作出相应的处理
 */
@Component
public class ItemListener {
    @Autowired
    private GoodsIndexService goodsIndexService;

    /**
     * 商品修改和添加时创建索引
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "ly.search.addGoodsIndexQueue", durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = {"ly.item.insert", "ly.item.update"}))
    public void insertItemHandler(Long spuId) {
        if (spuId == null) {
            return;
        }
        goodsIndexService.insertItemHandler(spuId);

    }

    /**
     * 商品删除时,删除索引
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "ly.search.deleteGoodsIndexQueue", durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "ly.item.delete"))
    public void deleteItemHandler(Long spuId) {
        if (spuId == null) {
            return;
        }
        goodsIndexService.deleteItemHandler(spuId);

    }
}
