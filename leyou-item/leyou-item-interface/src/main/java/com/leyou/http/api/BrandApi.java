package com.leyou.http.api;

import com.leyou.pojo.Brand;
import com.leyou.pojo.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/26 18:36
 * @desc
 */
@RequestMapping("brand")
public interface BrandApi {
    /**
     * 根据关键字分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("page")
    PageResult<Brand> getBrandListByPage(@RequestParam(value = "key", required = false) String key,
                                         @RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                         @RequestParam(value = "sortBy", required = false) String sortBy,
                                         @RequestParam(value = "desc", required = false) Boolean desc);


    @GetMapping("list")
    List<Brand> queryBrandListByIds(@RequestParam("cids") List<Long> cids);
}
