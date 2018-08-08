package com.leyou.pojo;

import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/21 14:32
 * @desc  vuetifyjs 表格组件 分页对象返回值
 */
public class PageResult<T> {
    //总条数
    private Long total;
    //当前页数据
    private List<T> items;
    //总页数
    private Integer totalPages;


    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getRows() {
        return Rows;
    }

    public void setRows(Integer rows) {
        Rows = rows;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    //每页显示数
    private Integer Rows;
    //当前页
    private Integer currentPage;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, List<T> items,Integer totalPages) {
        this.total = total;
        this.items = items;
        this.totalPages = totalPages;
    }

}
