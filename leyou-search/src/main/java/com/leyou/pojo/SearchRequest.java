package com.leyou.pojo;

import java.util.Map;

/**
 * @author zhoumo
 * @datetime 2018/7/27 16:19
 * @desc    搜索条件对象
 */
public class SearchRequest {

    //默认值
    private static final int DEFAULT_ROWS = 10;
    private static final int DEFAULT_PAGE = 1;
    //搜索关键字
    private String key;
    //每页显示数
    private Integer rows;
    //当前页
    private Integer page;
    //排序字段
    private String sortBy;
    //排序规格 降序/升序
    private Boolean descending;
    //规格参数
    private Map<String,String> filter;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getRows() {
        if(rows==null || rows<DEFAULT_ROWS){
            return DEFAULT_ROWS;
        }
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        if(this.page==null ||this.page<DEFAULT_PAGE){
            return DEFAULT_PAGE;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
