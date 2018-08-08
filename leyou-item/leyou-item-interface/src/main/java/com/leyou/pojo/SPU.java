package com.leyou.pojo;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/22 20:44
 * @desc 商品SPU公共属性集合
 */
@Table(name = "tb_spu")
public class SPU {
    /**
     * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'spu id',
     * `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
     * `sub_title` varchar(255) DEFAULT '' COMMENT '子标题',
     * `cid1` bigint(20) NOT NULL COMMENT '1级类目id',
     * `cid2` bigint(20) NOT NULL COMMENT '2级类目id',
     * `cid3` bigint(20) NOT NULL COMMENT '3级类目id',
     * `brand_id` bigint(20) NOT NULL COMMENT '商品所属品牌id',
     * `saleable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否上架，0下架，1上架',
     * `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效，0已删除，1有效',
     * `create_time` datetime DEFAULT NULL COMMENT '添加时间',
     * `last_update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String subTitle;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Long brandId;
    private Boolean saleable;
    private Boolean valid;
    private Date createTime;
    private Date lastUpdateTime;

    //分类名称
    @Transient
    private String cname;
    //商标名称
    @Transient
    private String bname;

    @Transient
    private List<SKU> skus;
    @Transient
    private SpuDetail spuDetail;

    public List<SKU> getSkus() {
        return skus;
    }

    public void setSkus(List<SKU> skus) {
        this.skus = skus;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Long getCid1() {
        return cid1;
    }

    public void setCid1(Long cid1) {
        this.cid1 = cid1;
    }

    public Long getCid2() {
        return cid2;
    }

    public void setCid2(Long cid2) {
        this.cid2 = cid2;
    }

    public Long getCid3() {
        return cid3;
    }

    public void setCid3(Long cid3) {
        this.cid3 = cid3;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Boolean getSaleable() {
        return saleable;
    }

    public void setSaleable(Boolean saleable) {
        this.saleable = saleable;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
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

    @Override
    public String toString() {
        return "SPU{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", cid1=" + cid1 +
                ", cid2=" + cid2 +
                ", cid3=" + cid3 +
                ", brandId=" + brandId +
                ", saleable=" + saleable +
                ", valid=" + valid +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", cname='" + cname + '\'' +
                ", bname='" + bname + '\'' +
                ", skus=" + skus +
                ", spuDetail=" + spuDetail +
                '}';
    }
}
