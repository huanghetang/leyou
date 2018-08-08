package com.leyou.search.service;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecificationClient;
import com.leyou.pojo.*;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.sms.utils.JsonUtils;
import com.leyou.sms.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhoumo
 * @datetime 2018/7/26 19:43
 * @desc 商品的索引服务
 */
@Service
public class GoodsIndexService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private BrandClient brandClient;


    //根据传入的SPU对象创建商品的索引对象
    public Goods CreateGoodsBySpu(SPU spu) {
        //创建商品对象并赋值普通属性
        Goods goods = new Goods();
        Long spuId = spu.getId();
        goods.setId(spuId);
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        //根据spuId查询所有sku
        List<SKU> skuList = goodsClient.getSKUListBySid(spuId);
        List<Long> price = new ArrayList<>();
        //用来生成skus json字符串的数据源
        ArrayList<Map<String, Object>> newSkuList = new ArrayList<Map<String, Object>>();
        for (SKU sku : skuList) {
            //拼接所有的sku价格信息,赋值
            price.add(sku.getPrice());
            //创建一个新对象赋值sku对象中的id,title,image,price
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            map.put("price", sku.getPrice());
            newSkuList.add(map);
        }
        goods.setPrice(price);
        //使用jackson构建json字符串 null属性需要配置不序列化
        goods.setSkus(JsonUtils.serialize(newSkuList));

        //根据cid123,分别查询商品分类,并拼接商品分类的名字
        List<Category> categoryList = categoryClient.
                queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> categoryNameList = categoryList.stream().map(Category::getName).collect(Collectors.toList());
        //以 / 拼接分类名称
        goods.setAll(spu.getTitle() + StringUtils.join(categoryNameList.toArray(), "/"));

        //specMap分为两部分,key为 spu的3级分类对应的spec_param对应的值
        List<SpecParam> specList = specificationClient.getSpecificationParamListByGid(null,
                spu.getCid3(), null, null, null, true);
        //specMap value为 spu_detail表里面 generic_spec和special_spec两部分
        SpuDetail spuDetail = goodsClient.getSpuDetailBySid(spuId);
        //获取通用规格参数键值对
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //获取sku规格参数键值对
        Map<Long, List> specialSpec = JsonUtils.parseMap(spuDetail.getSpecialSpec(), Long.class, List.class);
        //创建goods索引对象规格参数源数据
        Map<String, Object> specMap = new HashMap<String, Object>();
        //遍历规格参数的key,给规格参数元数据赋值
        for (SpecParam specParam : specList) {
            //规格参数id
            Long id = specParam.getId();
            String name = specParam.getName();
            if (specParam.getGeneric()) {//通用规格属性
                String value = genericSpec.get(id);
                if (specParam.getNumeric()) {//是不是值类型的属性,如果是需要分组
                    value = chooseSegment(value, specParam);
                }
                specMap.put(name, value);
            } else {//sku特有属性
                specMap.put(name, specialSpec.get(id));
            }
        }
        goods.setSpecs(specMap);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 根据搜索关键字分页查询商品
     *
     * @param searchRequest
     * @return
     */
    public PageResult<Goods> queryGoodsByKey(SearchRequest searchRequest) {
        int pages = searchRequest.getPage() - 1;
        int rows = searchRequest.getRows();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //设置需要的结果集字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        //查询条件,没有搜索条件时不查询
        if (StringUtils.isBlank(searchRequest.getKey())) {
            return null;
        }
        //创建基本查询和过滤条件
        QueryBuilder  basicQuery = buildBasicQueryBuilder(searchRequest);
        //添加查询和过滤条件
        queryBuilder.withQuery(basicQuery);
        //分页
        queryBuilder.withPageable(PageRequest.of(pages, rows));
        //排序

        //商品分类聚合
        String categoryAggName = "categoryAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //商品品牌聚合
        String brandAggName = "brandAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //执行查询
        AggregatedPage<Goods> page = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);
        //获取商品分类聚合
        LongTerms categoryAgg = (LongTerms) page.getAggregation(categoryAggName);
        //获取商品品牌聚合
        LongTerms brandAgg = (LongTerms) page.getAggregation(brandAggName);
        List<Category> categoryList = handlerCategoryAgg(categoryAgg);
        List<Brand> brandList = handlerBrandAgg(brandAgg);
        List<Map<String,Object>> specMap = null;
        if (categoryList != null && categoryList.size() == 1) {
            //根据分类id查询可以被过滤的规格参数集合
            List<SpecParam> specParamList = specificationClient.getSpecificationParamListByGid(null,
                    categoryList.get(0).getId(), null, null, null, true);
            //获取规格参数过滤集合
            specMap = handlerSpecList(specParamList, basicQuery);

        }
        //创建结果集并赋值
        SearchResult searchResult = new SearchResult();
        //赋值
        searchResult.setTotal(page.getTotalElements());
        searchResult.setItems(page.getContent());
        int total = (int) page.getTotalElements();
        searchResult.setTotalPages((total + rows - 1) / rows);
        searchResult.setCategoryList(categoryList);
        searchResult.setBrandList(brandList);
        searchResult.setSpecMapList(specMap);
        return searchResult;
    }
    //创建一个附带多个过滤条件的查询
    private QueryBuilder buildBasicQueryBuilder(SearchRequest searchRequest) {
        //构建组合查询(参考过滤条件的json结构)
        BoolQueryBuilder basicQueryBuilder = QueryBuilders.boolQuery();
        //添加查询条件
        basicQueryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).minimumShouldMatch("75%"));
        //添加过滤条件(多个过滤条件,过滤本身也是组合)
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        Map<String, String> filter = searchRequest.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if(!"cid3".equals(name) && !"brandId".equals(name)){
                name = "specs."+name+".keyword";
            }
            filterQueryBuilder.must(QueryBuilders.termQuery(name, value));
        }
        //把过滤条件添加到主查询条件
        basicQueryBuilder.filter(filterQueryBuilder);
        return basicQueryBuilder;
    }

    //添加过滤条件
    private void filterQueryBuilder(NativeSearchQueryBuilder queryBuilder, SearchRequest searchRequest) {
        Map<String, String> filter = searchRequest.getFilter();
        queryBuilder.withFilter(QueryBuilders.boolQuery());
    }

    //根据基本查询条件构建规格参数过滤集合
    private List<Map<String,Object>> handlerSpecList(List<SpecParam> specParamList, QueryBuilder basicQuery) {
        //到集合中的每个规格参数进行聚合,得到每一个规格参数的聚合结果
        List<String> specNameList = specParamList.stream().map(SpecParam::getName).collect(Collectors.toList());
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(basicQuery);
        for (String specName : specNameList) {
            builder.addAggregation(AggregationBuilders.terms(specName).field("specs."+specName+".keyword"));
        }
        AggregatedPage<Goods> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), Goods.class);
        //拿到每个聚合的key的部分是一个集合List<String>
        // 创建一个Map<key,Object> key是每一个规格参数的name 值就是这个List<String>
        ArrayList<Map<String,Object>> specMapList = new ArrayList<>();
        for (String specName : specNameList) {
            HashMap<String, Object> map = new HashMap<>();
            StringTerms specTerms = (StringTerms) aggregatedPage.getAggregation(specName);
            String type = specTerms.getType();
            List<String> specValList = specTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            //添加hashMap中然后赋值到RequestResult结果集中
            map.put("k",specName);
            map.put("options",specValList);
            specMapList.add(map);
        }
        return specMapList;
    }


    private List<Brand> handlerBrandAgg(LongTerms brandAgg) {
        List<Long> brandIdList = brandAgg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue())
                .collect(Collectors.toList());
        return brandClient.queryBrandListByIds(brandIdList);
    }

    //传入商品分类的聚和结果返回商品分类的集合
    private List<Category> handlerCategoryAgg(LongTerms categoryAgg) {
        //从桐里面获取cid
        List<Long> cidList = categoryAgg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue())
                .collect(Collectors.toList());

        //调用服务查询cid对应的分类对象
        return categoryClient.queryCategoryByIds(cidList);
    }

    public void insertItemHandler(Long spuId) {
        SPU spu = goodsClient.querySPUById(spuId);
        Goods goods = CreateGoodsBySpu(spu);
        goodsRepository.save(goods);
    }

    public void deleteItemHandler(Long spuId) {
        goodsRepository.deleteById(spuId);
    }
}
