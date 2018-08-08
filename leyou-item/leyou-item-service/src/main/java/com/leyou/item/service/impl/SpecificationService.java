package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/22 16:47
 * @desc 商品规格参数服务
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据商品分类id查询规格分组信息
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> querySpecGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        return specGroupList;
    }

    /**
     * 查询规格参数
     *
     * @param gid
     * @return
     */
    public List<SpecParam> querySpecParamListByGid(Long gid,Long cid,Long id,
                                           Boolean numeric,Boolean generic,Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setId(id);
        specParam.setNumeric(numeric);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return specParamMapper.select(specParam);
    }

    /**
     * 新增规格参数(商品分类对应的规格组对应的规格参数)
     *
     * @param specParam
     */
    public void addSpecParam(SpecParam specParam) {
        specParam.setId(null);
        specParamMapper.insert(specParam);
    }

    /**
     * 修改规格参数
     * @param specParam
     */
    public void editSpecParam(SpecParam specParam) {
        if(!specParam.getSearching()){//不做为搜素字段,让segments为空
            specParam.setSegments("");
        }
        if(!specParam.getNumeric()){//如果不是数值类型,把数值字段设置为空
            specParam.setUnit("");
        }
        specParamMapper.updateByPrimaryKeySelective(specParam);
    }

    /**
     * 删除规格参数
     * @param id
     */
    public void deleteSpecParamById(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }

    public List<SpecGroup> querySpecGroupListWithSpecParamByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        for (SpecGroup group : specGroupList) {
            //根据规格组id查询规格参数
            SpecParam specParam = new SpecParam();
            specParam.setGroupId(group.getId());
            List<SpecParam> specParamList = specParamMapper.select(specParam);
            //设置每个规格组中的规格参数信息
            group.setSpecParams(specParamList);
        }
        return specGroupList;

    }
}

