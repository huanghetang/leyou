package com.leyou.http.api;

import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/26 18:50
 * @desc
 */
@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据商品分类id查询规格分组信息
     *
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    List<SpecGroup> getSpecificationGroupListByCid(@PathVariable("cid") Long cid);

    /**
     * 查询规格参数
     *
     * @param gid
     * @return
     */
    @GetMapping("params")
    List<SpecParam> getSpecificationParamListByGid(@RequestParam(value = "gid", required = false) Long gid,
                                                   @RequestParam(value = "cid", required = false) Long cid,
                                                   @RequestParam(value = "id", required = false) Long id,
                                                   @RequestParam(value = "numeric", required = false) Boolean numeric,
                                                   @RequestParam(value = "generic", required = false) Boolean generic,
                                                   @RequestParam(value = "searching", required = false) Boolean searching);


    /**
     * 根据分类id查询带有多个规格参数的规格组列表
     *
     * @param cid
     * @return
     */
    @GetMapping("groups/specParam")
    List<SpecGroup> querySpecGroupListWithSpecParamByCid(@RequestParam("cid") Long cid);
}
