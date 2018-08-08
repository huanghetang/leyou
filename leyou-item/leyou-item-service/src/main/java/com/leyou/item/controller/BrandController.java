package com.leyou.item.controller;

import com.leyou.item.service.BrandService;
import com.leyou.pojo.Brand;
import com.leyou.pojo.PageResult;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/21 14:21
 * @desc 商品的商标接口
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * key: this.search, // 搜索条件
     * page: this.pagination.page,// 当前页
     * rows: this.pagination.rowsPerPage,// 每页大小
     * sortBy: this.pagination.sortBy,// 排序字段
     * desc: this.pagination.descending// 是否降序
     *
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> getBrandListByPage(@RequestParam(value = "key", required = false) String key,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                                @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                @RequestParam(value = "desc", required = false) Boolean desc) {
        PageResult<Brand> pageResult = brandService.queryBrandListByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(pageResult);

    }

    /**
     * 新增品牌
     * name: 黑马
     * image:
     * cids: 80
     * letter: H
     */
    @PostMapping("")
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam(value = "cids") List<String> cids) {
        System.out.println("brand = " + brand);
        System.out.println("cids = " + cids);
        if (brand == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        brandService.addBrand(brand, cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 修改品牌
     */
    @PutMapping
    public ResponseEntity editBrandByBrandId(Brand brand,@RequestParam(value="cids") List<Long> cids){
        brandService.editBrandByBrandId(brand,cids);
        return  new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 删除
     */
    @DeleteMapping("bid")
    public ResponseEntity editBrandByBrandId(@RequestParam("brandId") Long brandId){
        System.out.println("brandId = " + brandId);
        brandService.deleteBrandById(brandId);
        return  new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 根据品牌id查询品牌
     */
    @GetMapping("list")
    public  ResponseEntity<List<Brand>> queryBrandListByIds(@RequestParam("cids") List<Long> cids){
        List<Brand> brandList = brandService.queryBrandListByIds(cids);
        if (brandList == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(brandList);
    }

}
