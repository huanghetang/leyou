package vip.hht.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/8/4 17:48
 * @desc
 */
@ConfigurationProperties(prefix = "ly.zuul")
public class FilterProperties {
    private List<String> allowpath;

    public List<String> getAllowpath() {
        return allowpath;
    }

    public void setAllowpath(List<String> allowpath) {
        this.allowpath = allowpath;
    }
}
