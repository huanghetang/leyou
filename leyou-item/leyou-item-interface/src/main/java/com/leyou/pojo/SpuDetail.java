package com.leyou.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhoumo
 * @datetime 2018/7/23 16:15
 * @desc      spu详情,数据量太大,避免对常用字段的查询产生影响
 */
@Table(name="tb_spu_detail")
public class SpuDetail {
    /**
     *   `spu_id` bigint(20) NOT NULL,
     `description` text COMMENT '商品描述信息',
     `generic_spec` varchar(3000) NOT NULL DEFAULT '' COMMENT '通用规格参数数据',
     `special_spec` varchar(1000) NOT NULL COMMENT '特有规格参数及可选值信息，json格式',
     `packing_list` varchar(1000) DEFAULT '' COMMENT '包装清单',
     `after_service` varchar(1000) DEFAULT '' COMMENT '售后服务',
     */

    @Id
    private Long spuId;
    private String description;
    private String genericSpec;
    private String specialSpec;
    private String packingList;
    private String afterService;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenericSpec() {
        return genericSpec;
    }

    public void setGenericSpec(String genericSpec) {
        this.genericSpec = genericSpec;
    }

    public String getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(String specialSpec) {
        this.specialSpec = specialSpec;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }

    @Override
    public String toString() {
        return "SpuDetail{" +
                "spuId=" + spuId +
                ", description='" + description + '\'' +
                ", genericSpec='" + genericSpec + '\'' +
                ", specialSpec='" + specialSpec + '\'' +
                ", packingList='" + packingList + '\'' +
                ", afterService='" + afterService + '\'' +
                '}';
    }
}
