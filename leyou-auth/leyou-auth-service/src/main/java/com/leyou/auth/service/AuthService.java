package com.leyou.auth.service;

import com.leyou.auth.client.UserQueryClient;
import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.utils.JwtUtils;
import com.leyou.auth.config.JwtProperties;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author zhoumo
 * @datetime 2018/8/3 17:07
 * @desc
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    @Autowired
    private UserQueryClient userQueryClient;
    @Autowired
    private JwtProperties props;
//    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public String accredit(String username, String password) {
        try {
            //调用用户服务查询用户信息
            User user = userQueryClient.query(username, password);
            //登陆失败
            if (user == null) {
                return null;
            }
            //生成token 返回给用户
            UserInfo userInfo = new UserInfo(user.getId(), user.getUsername());

            //生成公钥和密匙然后生成token,token有效期30分钟
            String token = JwtUtils.generateToken(userInfo, props.getPrivateKey(), props.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }


    }
}
