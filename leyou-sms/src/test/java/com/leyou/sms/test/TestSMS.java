package com.leyou.sms.test;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.sms.utils.SmsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @author zhoumo
 * @datetime 2018/8/2 14:17
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSMS {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private SmsUtils smsUtils;

    @Test
    public void test1() throws InterruptedException {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone","15702127514");
        map.put("code","1234");
        amqpTemplate.convertAndSend("ly.sendPhoneCode.exchange","ly.sms.sendPhoneCode",map);
        Thread.sleep(10000);

    }

    @Test
    public void test111() throws InterruptedException {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone","15702127514");
        map.put("code","123456");
        try {
            smsUtils.sendSms("15702127514", "123456", "zmtest", "zmyzm");
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }

}
