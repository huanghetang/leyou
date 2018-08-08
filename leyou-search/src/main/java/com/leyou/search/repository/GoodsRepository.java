package com.leyou.search.repository;

import com.leyou.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zhoumo
 * @datetime 2018/7/27 9:28
 * @desc    商品索引库的增删改查
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
