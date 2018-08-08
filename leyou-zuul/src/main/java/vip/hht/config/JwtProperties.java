package vip.hht.config;

import com.leyou.auth.common.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author zhoumo
 * @datetime 2018/8/4 17:03
 * @desc
 */
@ConfigurationProperties("ly.zuul.jwt")
public class JwtProperties {

    private String pubKeyPath; //公钥地址
    private String cookieName; //token名字

    private PublicKey publicKey;
    private Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init() {
        if (publicKey == null) {
            try {
                publicKey = RsaUtils.getPublicKey(pubKeyPath);
            } catch (Exception e) {
                logger.error("创建公钥失败!");
                e.printStackTrace();
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

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
