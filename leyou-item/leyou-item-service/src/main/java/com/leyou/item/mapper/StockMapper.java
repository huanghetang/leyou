package com.leyou.item.mapper;

import com.leyou.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 * @author zhoumo
 * @datetime 2018/7/23 17:07
 * @desc
 */
public interface StockMapper extends Mapper<Stock>,InsertListMapper<Stock>,DeleteByIdListMapper<Stock,Long> {
}
