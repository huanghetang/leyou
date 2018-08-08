package com.leyou.pojo;

/**
 * @author zhoumo
 * @datetime 2018/7/22 15:34
 * @desc  分类商标中间表数据
 */
public class CategoryBrand {
    /**
     *   `category_id` bigint(20) NOT NULL COMMENT '商品类目id',
     `brand_id` bigint(20) NOT NULL COMMENT '品牌id',
     */

    private Long categoryId;
    private Long brandId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "CategoryBrand{" +
                "categoryId=" + categoryId +
                ", brandId=" + brandId +
                '}';
    }
}
