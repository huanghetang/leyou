package com.leyou.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhoumo
 * @datetime 2018/7/19 20:12
 * @desc `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类目id',
 * `name` varchar(20) NOT NULL COMMENT '类目名称',
 * `parent_id` bigint(20) NOT NULL COMMENT '父类目id,顶级类目填0',
 * `is_parent` tinyint(1) NOT NULL COMMENT '是否为父节点，0为否，1为是',
 * `sort` int(4) NOT NULL COMMENT '排序指数，越小越靠前',
 */

/**
 * id
 isParent

 name


 parentId

 2
 sort

 6
 */
@Table(name="tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean parent) {
        isParent = parent;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", isParent=" + isParent +
                ", sort=" + sort +
                '}';
    }
}
