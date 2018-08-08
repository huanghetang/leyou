package com.leyou.cart.config;

import com.leyou.auth.common.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author zhoumo
 * @datetime 2018/8/5 16:42
 * @desc
 */
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);
    private String pubKeyPath; //F:/hm/ssh/rsa.pub # 公钥地址
    private String cookieName; //LY_TOKEN
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        if (publicKey == null) {
            try {
                this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            } catch (Exception e) {
                logger.error("初始化公钥失败!", e);
            }
        }
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
