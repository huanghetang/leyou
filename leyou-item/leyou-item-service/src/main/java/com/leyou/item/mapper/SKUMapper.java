package com.leyou.item.mapper;

import com.leyou.pojo.SKU;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhoumo
 * @datetime 2018/7/23 16:47
 * @desc
 */
public interface SKUMapper extends Mapper<SKU>,DeleteByIdListMapper<SKU,Long>{
}
