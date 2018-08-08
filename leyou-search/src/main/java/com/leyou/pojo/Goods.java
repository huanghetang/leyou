package com.leyou.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/7/26 16:46
 * @desc  商品的搜索和索引对象 在spu的基础上拓展
 */
@Document(indexName = "goods",type = "docs",shards = 1,replicas = 0)
public class Goods {
    //主键
    @Id
    private Long id;

    //搜索关键字对应的索引 all = title+商品分类
    @Field(type = FieldType.text,analyzer = "ik_max_word")
    private String all;

    //子标题
    @Field(type = FieldType.keyword,index = false)
    private String subTitle;

    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Long brandId;
    private Date createTime;

    //价格
    private List<Long> price;

    //sku对象中的id,subTitle,image,price
    @Field(type = FieldType.keyword,index = false)
    private String skus;

    //规格参数map集合
    private Map<String,Object> specs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Long> getPrice() {
        return price;
    }

    public void setPrice(List<Long> price) {
        this.price = price;
    }

    public String getSkus() {
        return skus;
    }

    public void setSkus(String skus) {
        this.skus = skus;
    }

    public Map<String, Object> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, Object> specs) {
        this.specs = specs;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", all='" + all + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", cid1=" + cid1 +
                ", cid2=" + cid2 +
                ", cid3=" + cid3 +
                ", brandId=" + brandId +
                ", createTime=" + createTime +
                ", price=" + price +
                ", skus='" + skus + '\'' +
                ", specs=" + specs +
                '}';
    }
}
