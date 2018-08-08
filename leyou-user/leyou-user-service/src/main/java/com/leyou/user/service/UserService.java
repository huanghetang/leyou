package com.leyou.user.service;

import com.leyou.sms.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoumo
 * @datetime 2018/8/2 16:06
 * @desc
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String PHONE_CODE_ROUTINKEY = "ly.sms.sendPhoneCode";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Value("${code.redis.prefix}")
    private String PREFIX;

    //要校验的数据类型：1，用户名；2，手机；3，邮箱
    //返回布尔类型结果：
    //- true：可用
    //- false：不可用
    public Boolean verify(String data, Integer type) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                return null;
        }
        int i = userMapper.selectCount(user);
        return i <= 0;
    }

    /**
     * 根据用户输入的手机号，生成随机验证码，长度为6位，纯数字。并且调用短信服务，发送验证码到用户手机。
     *
     * @param phone
     */
    public boolean sendCode(String phone) {
        try {
            //生成6位数的验证码
            String code = NumberUtils.generateCode(6);
            //保存到redis
            String key = PREFIX + ":" + phone;
            redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
            //发送mq通知sms服务发送验证码短信
            //创建短信内容
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("code", code);
            amqpTemplate.convertAndSend(PHONE_CODE_ROUTINKEY, map);
            return true;
        } catch (Exception e) {
            logger.error("发送验证码失败phone{}" + phone, e);
            return false;
        }
    }

    public Boolean register(User user, String code) {
        //校验
        if (StringUtils.isBlank(code)) {
            return false;
        }
        //code和redis中的比较
        String key = PREFIX + ":" + user.getPhone();
        String redisVerifyCode = redisTemplate.opsForValue().get(key);
        if (!code.equals(redisVerifyCode)) {
            return false;
        }
        //取出密码 使用加密规格进行加密
        String password = user.getPassword();
        //生成盐
        String salt = CodecUtils.generateSalt();
        String storePassword = CodecUtils.md5Hex(password, salt);
        //保存到数据库
        User record = new User();
        record.setUsername(user.getUsername());
        record.setPassword(storePassword);
        record.setPhone(user.getPhone());
        record.setSalt(salt);
        record.setCreated(new Date());
        boolean b = userMapper.insertSelective(record) == 1;
        //删除redis数据
        if (b) {
            try {
                redisTemplate.delete(PREFIX + ":" + user.getPhone());
            } catch (Exception e) {
                logger.error("删除缓存中的手机验证码失败,key{}", PREFIX + ":" + user.getPhone(), e);
                return true;
            }
        }
        return true;
    }

    public User query( User user) {
        //根据用户名查询用户
        User t = new User();
        t.setUsername(user.getUsername());
        User expectUser = userMapper.selectOne(t);
        if(expectUser==null){
            //用户名错误
            logger.error("用户名错误!username{}"+user.getUsername());
            return null;
        }
        //获取查询到的用户密码
        // 对用户填写的密码+salt加密
        String verifyPassword = CodecUtils.md5Hex(user.getPassword(), expectUser.getSalt());
        //比较数据库中的密码和运算出的密码是否相等
        if(!StringUtils.equals(verifyPassword,expectUser.getPassword())){
            //密码错误
            logger.error("密码错误!password{}"+user.getPassword());
            return null;
        }
        //相等返回对象
        return expectUser;
    }
}
