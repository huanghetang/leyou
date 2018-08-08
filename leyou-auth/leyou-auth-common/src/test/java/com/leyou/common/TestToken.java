package com.leyou.common;

import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.utils.JwtUtils;
import com.leyou.auth.common.utils.RsaUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author zhoumo
 * @datetime 2018/8/3 22:15
 * @desc
 */
public class TestToken {

    public static void main(String[] args) throws Exception {
        String publicKeyFilename = "F:\\hm\\ssh\\rsa.pub";
        String privateKeyFilename = "F:\\hm\\ssh\\rsa.pri";
//        String secret = "helloWorld";
//        RsaUtils.generateKey(publicKeyFilename,privateKeyFilename,secret);
        UserInfo jt = new UserInfo(1l, "景甜");
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateKeyFilename);
        String token = JwtUtils.generateTokenInSeconds(jt, privateKey, 2);
        Thread.sleep(3000);
        System.out.println("token = " + token);
        PublicKey publicKey = RsaUtils.getPublicKey(publicKeyFilename);
        UserInfo info = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("info.getUsername() = " + info.getUsername());

    }
}
