package com.leyou.pojo;

import javax.persistence.*;

/**
 * @author zhoumo
 * @datetime 2018/7/22 17:06
 * @desc 商品规格参数
 */
@Table(name="tb_spec_param")
public class SpecParam {
    /**
     *   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `cid` bigint(20) NOT NULL COMMENT '商品分类id',
     `group_id` bigint(20) NOT NULL,
     `name` varchar(255) NOT NULL COMMENT '参数名',
     `numeric` tinyint(1) NOT NULL COMMENT '是否是数字类型参数，true或false',
     `unit` varchar(255) DEFAULT '' COMMENT '数字类型参数的单位，非数字类型可以为空',
     `generic` tinyint(1) NOT NULL COMMENT '是否是sku通用属性，true或false',
     `searching` tinyint(1) NOT NULL COMMENT '是否用于搜索过滤，true或false',
     `segments` varchar(1000) DEFAULT '' COMMENT '数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0',
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNumeric() {
        return numeric;
    }

    public void setNumeric(Boolean numeric) {
        this.numeric = numeric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getGeneric() {
        return generic;
    }

    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }

    public Boolean getSearching() {
        return searching;
    }

    public void setSearching(Boolean searching) {
        this.searching = searching;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return "SpecParam{" +
                "id=" + id +
                ", cid=" + cid +
                ", groupId=" + groupId +
                ", name='" + name + '\'' +
                ", numeric=" + numeric +
                ", unit='" + unit + '\'' +
                ", generic=" + generic +
                ", searching=" + searching +
                ", segments='" + segments + '\'' +
                '}';
    }
}
