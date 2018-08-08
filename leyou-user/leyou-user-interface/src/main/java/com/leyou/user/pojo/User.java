package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author zhoumo
 * @datetime 2018/8/2 15:43
 * @desc
 */
@Table(name = "tb_user")
public class User {

    /**
     * `id` bigint(20) NOT NULL AUTO_INCREMENT,
     * `username` varchar(50) NOT NULL COMMENT '用户名',
     * `password` varchar(32) NOT NULL COMMENT '密码，加密存储',
     * `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
     * `created` datetime NOT NULL COMMENT '创建时间',
     * `salt` varchar(32) NOT NULL COMMENT '密码加密的salt值',
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2,max = 30)
    private String username;
    @JsonIgnore
    @Size(min = 3,max = 30,message = "密码长度要在4~30之间")
    private String password;
    @Pattern(regexp = "^1[35678]\\d{9}$",message = "手机号格式不正确")
    private String phone;
    private Date created;
    @JsonIgnore
    private String salt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
