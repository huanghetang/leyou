package com.leyou.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhoumo
 * @datetime 2018/7/23 16:04
 * @desc 库存单位(具体的商品)
 */
@Table(name = "tb_sku")
public class SKU {
    /**
     * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'sku id',
     * `spu_id` bigint(20) NOT NULL COMMENT 'spu id',
     * `title` varchar(255) NOT NULL COMMENT '商品标题',
     * `images` varchar(1000) DEFAULT '' COMMENT '商品的图片，多个图片以‘,’分割',
     * `price` bigint(15) NOT NULL DEFAULT '0' COMMENT '销售价格，单位为分',
     * `indexes` varchar(100) DEFAULT '' COMMENT '特有规格属性在spu属性模板中的对应下标组合',
     * `own_spec` varchar(1000) DEFAULT '' COMMENT 'sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序',
     * `enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效，0无效，1有效',
     * `create_time` datetime NOT NULL COMMENT '添加时间',
     * `last_update_time` datetime NOT NULL COMMENT '最后修改时间',
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String indexes;
    private String ownSpec;
    private Boolean enable;
    private Date createTime;
    private Date lastUpdateTime;
    @Transient
    private Integer stock;//库存

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getIndexes() {
        return indexes;
    }

    public void setIndexes(String indexes) {
        this.indexes = indexes;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
