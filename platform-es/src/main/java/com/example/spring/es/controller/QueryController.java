package com.example.spring.es.controller;

import cn.hutool.core.collection.CollectionUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.SuggestMode;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.TermSuggester;
import co.elastic.clients.elasticsearch.sql.QueryResponse;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.spring.es.entity.Poet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName QueryController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/24 10:09
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("es/docQuery/")
@RequiredArgsConstructor
@Api(tags = "es 查询操作", consumes = "application/json", produces = "application/json", protocols = "http")
public class QueryController {
    private final ElasticsearchClient client;

    private static String INDEX_NAME = "index_home";

    @GetMapping("getDocAll")
    @ApiOperation(value = "1查看文档", notes = "查看文档")
    public void getDocAll() throws IOException {
        SearchResponse<Poet> response = client.search(builder -> builder.index(INDEX_NAME), Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }

    @GetMapping("getDoc")
    @ApiOperation(value = "2查看单个文档", notes = "查看文档")
    public void getDoc() throws IOException {
        GetResponse<Map> response = client.get(builder -> builder.index(INDEX_NAME).id("10"), Map.class);
        if (response.found()) {
            log.info(response.source().toString());
        }

        GetResponse<Poet> response2 = client.get(builder -> builder.index(INDEX_NAME).id("20"), Poet.class);
        if (response2.found()) {
            log.info(response2.source().toString());
        }
    }

    @GetMapping("searchTerm")
    @ApiOperation(value = "3 term/terms 查询", notes = "term/terms 查询")
    public void searchTerm() throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .term(termQueryBuilder -> termQueryBuilder
                                        .field("name").value("李白0")))
                        .sort(sortOptionsBuilder -> sortOptionsBuilder
                                .field(fieldSortBuilder -> fieldSortBuilder
                                        .field("name").order(SortOrder.Asc)))
                        .source(sourceConfigBuilder -> sourceConfigBuilder
                                .filter(sourceFilterBuilder -> sourceFilterBuilder
                                        .includes("age", "name")))
                        .from(0)
                        .size(10)
                , Map.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        List<FieldValue> words = new ArrayList<>();
        words.add(new FieldValue.Builder().stringValue("李白2").build());
        words.add(new FieldValue.Builder().stringValue("杜甫2").build());
        SearchResponse<Poet> response2 = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .terms(termsQueryBuilder -> termsQueryBuilder
                                        .field("name").terms(termsQueryFieldBuilder -> termsQueryFieldBuilder.value(words))))
                        .source(sourceConfigBuilder -> sourceConfigBuilder
                                .filter(sourceFilterBuilder -> sourceFilterBuilder
                                        .excludes("about")))
                        .from(0)
                        .size(10)
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response2.hits().hits())){
            response2.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchRange")
    @ApiOperation(value = "4 范围 查询", notes = "范围 查询")
    public void searchRange() throws IOException {
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .range(rangeQueryBuilder -> rangeQueryBuilder
                                        .field("age").gte(JsonData.of("20")).lt(JsonData.of("40"))))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchMatch")
    @ApiOperation(value = "5 全文 查询", notes = "全文 查询")
    public void searchMatch() throws IOException {
        SearchResponse<Map> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field("success").query("思想")))
                , Map.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchMultiMatch")
    @ApiOperation(value = "6 多个字段进行匹配查询", notes = "多个字段进行匹配查询")
    public void searchMultiMatch() throws IOException {
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .multiMatch(multiMatchQueryBuilder -> multiMatchQueryBuilder
                                        .fields("about", "success").query("思想")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchMatchPhrase")
    @ApiOperation(value = "7 匹配整个查询字符串", notes = "匹配整个查询字符串")
    public void searchMatchPhrase() throws IOException {
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .matchPhrase(matchPhraseQueryBuilder -> matchPhraseQueryBuilder.field("success").query("文学作家")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchMatchAll")
    @ApiOperation(value = "8 查询整个文档", notes = "查询整个文档")
    public void searchMatchAll() throws IOException {
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .matchAll(matchAllQueryBuilder -> matchAllQueryBuilder))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchQueryString")
    @ApiOperation(value = "9 query_string 可以同时实现前面几种查询方法", notes = "query_string 可以同时实现前面几种查询方法")
    public void searchQueryString() throws IOException {
        //类似 match
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .defaultField("success").query("古典文学")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //类似 mulit_match
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("about", "success").query("古典文学")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //类似 match_phrase
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .defaultField("success").query("\"文学作家\"")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //带运算符查询，运算符两边的词不再分词
        //查询同时包含 ”文学“ 和 ”伟大“ 的文档
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("success").query("文学 AND 伟大")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //等同上一个查询
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("success").query("文学 伟大").defaultOperator(Operator.And)))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //查询 name 或 success 字段包含"文学"和"伟大"这两个单词，或者包含"李白"这个单词的文档。
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .queryString(queryStringQueryBuilder -> queryStringQueryBuilder
                                        .fields("name","success").query("(文学 AND 伟大) OR 高度")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }

    /**
     * 1、不支持AND OR NOT ，会当做字符处理；使用 + 代替 AND，| 代替OR，- 代替 NOT
     * 2、会忽略错误的语法
     * @throws IOException
     */
    @GetMapping("searchSimpleQueryString")
    @ApiOperation(value = "10 类似 query_string", notes = "类似 query_string")
    public void searchSimpleQueryString() throws IOException {
        //查询同时包含 ”文学“ 和 ”伟大“ 的文档
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .simpleQueryString(simpleQueryStringQueryBuilder -> simpleQueryStringQueryBuilder
                                        .fields("success").query("文学 + 伟大")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchFuzzy")
    @ApiOperation(value = "11 模糊查询", notes = "模糊查询")
    public void searchFuzzy() throws IOException {
        //全文查询时使用模糊参数，先分词再计算模糊选项。
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field("success")
                                        .query("思考")
                                        .fuzziness("1")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //使用 fuzzy query，对输入不分词，直接计算模糊选项。
        SearchResponse<Poet> response2 = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .fuzzy(fuzzyQueryBuilder ->  fuzzyQueryBuilder
                                        .field("success").fuzziness("1").value("理想")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }

    @GetMapping("searchBool")
    @ApiOperation(value = "12 组合查询", notes = "组合查询")
    public void searchBool() throws IOException {
        //查询 success 包含 “思想” 且 age 在 [20-40] 之间的文档
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .must(queryBuilder2 -> queryBuilder2
                                                .match(matchQueryBuilder -> matchQueryBuilder
                                                        .field("success").query("思想"))
                                        )
                                        .must(queryBuilder2 -> queryBuilder2
                                                .range(rangeQueryBuilder -> rangeQueryBuilder
                                                        .field("age").gte(JsonData.of("20")).lt(JsonData.of("40")))
                                        )
                                )
                        )
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //过滤出 success 包含 “思想” 且 age 在 [20-40] 之间的文档，不计算得分
        SearchResponse<Poet> response2 = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .filter(queryBuilder2 -> queryBuilder2
                                                .match(matchQueryBuilder -> matchQueryBuilder
                                                        .field("success").query("思想"))
                                        )
                                        .filter(queryBuilder2 -> queryBuilder2
                                                .range(rangeQueryBuilder -> rangeQueryBuilder
                                                        .field("age").gte(JsonData.of("20")).lt(JsonData.of("40")))
                                        )
                                )
                        )
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchAggs")
    @ApiOperation(value = "13 聚合查询", notes = "聚合查询")
    public void searchAggs() throws IOException {
        //求和
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("age_sum", aggregationBuilder -> aggregationBuilder
                                .sum(sumAggregationBuilder -> sumAggregationBuilder
                                        .field("age")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //类似 select count distinct(age) from poet-index
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("age_count", aggregationBuilder -> aggregationBuilder
                                .cardinality(cardinalityAggregationBuilder -> cardinalityAggregationBuilder.field("age")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //数量、最大、最小、平均、求和
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("age_stats", aggregationBuilder -> aggregationBuilder
                                .stats(statsAggregationBuilder -> statsAggregationBuilder
                                        .field("age")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //select name,count(*) from poet-index group by name
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("name_terms", aggregationBuilder -> aggregationBuilder
                                .terms(termsAggregationBuilder -> termsAggregationBuilder
                                        .field("name")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //select name,age,count(*) from poet-index group by name,age
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .aggregations("name_terms", aggregationBuilder -> aggregationBuilder
                                .terms(termsAggregationBuilder -> termsAggregationBuilder
                                        .field("name")
                                )
                                .aggregations("age_terms", aggregationBuilder2 -> aggregationBuilder2
                                        .terms(termsAggregationBuilder -> termsAggregationBuilder
                                                .field("age")
                                        ))
                        )
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }

        //类似 select avg(age) from poet-index where name='李白'
        response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .bool(boolQueryBuilder -> boolQueryBuilder
                                        .filter(queryBuilder2 -> queryBuilder2
                                                .term(termQueryBuilder -> termQueryBuilder
                                                        .field("name").value("李白")))))
                        .aggregations("ave_age", aggregationBuilder -> aggregationBuilder
                                .avg(averageAggregationBuilder -> averageAggregationBuilder.field("age")))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchSuggest")
    @ApiOperation(value = "14 推荐搜索", notes = "推荐搜索")
    public void searchSuggest() throws IOException {

        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .suggest(suggesterBuilder -> suggesterBuilder
                                .suggesters("success_suggest", fieldSuggesterBuilder -> fieldSuggesterBuilder
                                        .term(termSuggesterBuilder -> termSuggesterBuilder
                                                .field("success")
                                                .text("思考")
                                                .suggestMode(SuggestMode.Always)
                                                .minWordLength(2)
                                        )
                                )
                        )
                , Poet.class);

        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchHighlight")
    @ApiOperation(value = "高亮显示", notes = "高亮显示")
    public void searchHighlight() throws IOException {
        SearchResponse<Poet> response = client.search(searchRequestBuilder -> searchRequestBuilder
                        .index(INDEX_NAME)
                        .query(queryBuilder -> queryBuilder
                                .match(matchQueryBuilder -> matchQueryBuilder
                                        .field("success").query("思想")))
                        .highlight(highlightBuilder -> highlightBuilder
                                .preTags("<span color='red'>")
                                .postTags("</span>")
                                .fields("success", highlightFieldBuilder -> highlightFieldBuilder))
                , Poet.class);
        if(CollectionUtil.isNotEmpty(response.hits().hits())){
            response.hits().hits().forEach(e ->{
                log.info(JSONObject.toJSONString(e.source()));
            });
        }
    }
    @GetMapping("searchSql")
    @ApiOperation(value = "sql查询", notes = "sql查询")
    public void searchSql() throws IOException {
        QueryResponse response = client.sql().query(builder -> builder
                .format("json").query("SELECT * FROM \"" + INDEX_NAME + "\" limit 3"));
        log.info(response.toString());
    }

}
