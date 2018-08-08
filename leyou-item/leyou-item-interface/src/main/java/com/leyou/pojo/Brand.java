package com.leyou.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhoumo
 * @datetime 2018/7/21 14:30
 * @desc 商标实体
 */
@Table(name = "tb_brand")
public class Brand {
    /**
     * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '品牌id',
     * `name` varchar(50) NOT NULL COMMENT '品牌名称',
     * `image` varchar(200) DEFAULT '' COMMENT '品牌图片地址',
     * `letter` char(1) DEFAULT '' COMMENT '品牌的首字母',
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    private Character letter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Character getLetter() {
        return letter;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", letter=" + letter +
                '}';
    }
}
