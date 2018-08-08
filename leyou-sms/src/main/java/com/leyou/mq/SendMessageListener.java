package com.leyou.mq;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/8/2 7:34
 * @desc 发送短信mq监听器
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SendMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(SendMessageListener.class);
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private SmsProperties prop;

    /**
     * 发送短信验证码
     *
     * @param map 包含phone:xxx, code:xxx的map,发送失败时mq消息不回滚
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "ly.sms.sendPhoneCodeQueue", durable = "true"),
            exchange = @Exchange(name = "ly.sendPhoneCode.exchange", type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"), key = {"ly.sms.sendPhoneCode"}))
    public void sendMessage(Map<String, String> map) {
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        String phone = map.get("phone");
        String code = map.get("code");
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            return;
        }
        try {
            SendSmsResponse res = smsUtils.sendSms(phone, code, prop.getSignName(), prop.getVerifyCodeTemplate());
            if (!"OK".equalsIgnoreCase(res.getCode())) {
                logger.error("验证码发送失败!phone{}:code{}:",phone,code);
            }
        }catch (Exception e){
            logger.error("验证码发送失败!phone{}:code{}:",phone,code,e);
        }



    }
}
