package com.leyou.item.controller;

import com.leyou.item.service.impl.SpecificationService;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/22 16:35
 * @desc  商品规格接口
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specService;

    /**
     * 根据商品分类id查询规格分组信息
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> getSpecificationGroupListByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> specGroupList = specService.querySpecGroupByCid(cid);
        if(CollectionUtils.isEmpty(specGroupList)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(specGroupList);
    }

    /**
     * 查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> getSpecificationParamListByGid(@RequestParam(value = "gid",required = false) Long gid,
                                                         @RequestParam(value = "cid",required = false) Long cid,
                                                         @RequestParam(value = "id",required = false) Long id,
                                                         @RequestParam(value = "numeric",required = false) Boolean numeric,
                                                         @RequestParam(value = "generic",required = false) Boolean generic,
                                                         @RequestParam(value = "searching",required = false) Boolean searching){
       List<SpecParam> specParamList =  specService.querySpecParamListByGid(gid,cid,id,numeric,generic,searching);
       if(specParamList==null){
           return new ResponseEntity(HttpStatus.NOT_FOUND);
       }
        return  ResponseEntity.ok(specParamList);
    }


    /**
     * 根据分类id查询带有多个规格参数的规格组列表
     * @param cid
     * @return
     */
    @GetMapping("groups/specParam")
    public ResponseEntity<List<SpecGroup>> querySpecGroupListWithSpecParamByCid(@RequestParam("cid") Long cid){
        List<SpecGroup> specGroupListWithSpecParam = specService.querySpecGroupListWithSpecParamByCid(cid);
        if(specGroupListWithSpecParam==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(specGroupListWithSpecParam);
    }

    /**
     *  新增规格参数
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity addSpecParam(@RequestBody SpecParam specParam){
        System.out.println("specParam = " + specParam);
        specService.addSpecParam(specParam);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 修改规格参数
     */
    @PutMapping("param")
    public ResponseEntity editSpecParam(@RequestBody SpecParam specParam){
        System.out.println("specParam = " + specParam);
        specService.editSpecParam(specParam);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 根据主键删除规格参数
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity deleteSpecParam(@PathVariable(value="id") Long id){
        specService.deleteSpecParamById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
