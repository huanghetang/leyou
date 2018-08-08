package com.leyou.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhoumo
 * @datetime 2018/7/23 17:00
 * @desc SKU对应的库存
 */
@Table(name = "tb_stock")
public class Stock {
    /**
     * `sku_id` bigint(20) NOT NULL COMMENT '库存对应的商品sku id',
     * `seckill_stock` int(9) DEFAULT '0' COMMENT '可秒杀库存',
     * `seckill_total` int(9) DEFAULT '0' COMMENT '秒杀总数量',
     * `stock` int(9) NOT NULL COMMENT '库存数量',
     * PRIMARY KEY (`sku_id`)
     */
//    @Column(name = "sku_id")
    @Id
    private Long skuId;
    private Integer seckillStock;
    private Integer seckillTotal;
    private Integer stock;

    @Override
    public String toString() {
        return "Stock{" +
                "skuId=" + skuId +
                ", seckillStock=" + seckillStock +
                ", seckillTotal=" + seckillTotal +
                ", stock=" + stock +
                '}';
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getSeckillStock() {
        return seckillStock;
    }

    public void setSeckillStock(Integer seckillStock) {
        this.seckillStock = seckillStock;
    }

    public Integer getSeckillTotal() {
        return seckillTotal;
    }

    public void setSeckillTotal(Integer seckillTotal) {
        this.seckillTotal = seckillTotal;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
