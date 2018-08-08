package com.leyou.pojo;

import javax.persistence.*;
import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/22 16:43
 * @desc 商品规格的规格组数据
 */
@Table(name="tb_spec_group")
public class SpecGroup {
    /**
     * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     * `cid` bigint(20) NOT NULL COMMENT '商品分类id，一个分类下有多个规格组',
     * `name` varchar(50) NOT NULL COMMENT '规格组的名称',
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private String name;

    @Override
    public String toString() {
        return "SpecGroup{" +
                "id=" + id +
                ", cid=" + cid +
                ", name='" + name + '\'' +
                ", specParams=" + specParams +
                '}';
    }

    @Transient
    private List<SpecParam> specParams;

    public List<SpecParam> getSpecParams() {
        return specParams;
    }

    public void setSpecParams(List<SpecParam> specParams) {
        this.specParams = specParams;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
