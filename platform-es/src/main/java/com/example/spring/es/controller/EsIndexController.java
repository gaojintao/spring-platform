package com.example.spring.es.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.PutMappingResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @ClassName EsIndexController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/19 17:12
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("es/index/")
@RequiredArgsConstructor
@Api(tags = "es索引 操作", consumes = "application/json", produces = "application/json", protocols = "http")
public class EsIndexController {
    private final ElasticsearchClient elasticsearchClient;

    private static String INDEX_NAME = "index_home";

    @GetMapping("createIndex")
    @ApiOperation(value = "创建", notes = "创建")
    public String createIndex() throws IOException {
        CreateIndexResponse createResponse = elasticsearchClient.indices().create(demo -> demo.index("demo1"));


        return Boolean.TRUE.equals(createResponse.acknowledged()) ? "创建成功" : "创建失败";
    }

    @GetMapping("createIndex2")
    @ApiOperation(value = "创建索引2", notes = "创建索引2")
    public void createIndex2() throws IOException {
        CreateIndexResponse response = elasticsearchClient.indices().create(builder -> builder
                .settings(indexSettingsBuilder -> indexSettingsBuilder.numberOfReplicas("1").numberOfShards("2"))
                .mappings(typeMappingBuilder -> typeMappingBuilder
                        .properties("age", propertyBuilder -> propertyBuilder.integer(integerNumberPropertyBuilder -> integerNumberPropertyBuilder))
                        .properties("name", propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder))
                        .properties("poems", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_max_word")))
                        .properties("about", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_max_word")))
                        .properties("success", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_max_word")))
                )
                .index(INDEX_NAME));
        log.info("acknowledged={}", response.acknowledged());
 }
    @GetMapping("modifyIndex")
    @ApiOperation(value = "修改索引", notes = "修改索引")
    public void modifyIndex() throws IOException {
        PutMappingResponse response = elasticsearchClient.indices().putMapping(typeMappingBuilder -> typeMappingBuilder
                .index(INDEX_NAME)
                .properties("age", propertyBuilder -> propertyBuilder.integer(integerNumberPropertyBuilder -> integerNumberPropertyBuilder))
                .properties("name", propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder))
                .properties("poems", propertyBuilder -> propertyBuilder.text(textPropertyBuilder -> textPropertyBuilder.analyzer("ik_max_word").searchAnalyzer("ik_smart")))
        );
        log.info("acknowledged={}", response.acknowledged());
    }

    @GetMapping("searchIndexList")
    @ApiOperation(value = "查询", notes = "查询")

    public String searchIndexList() throws IOException {
        GetIndexResponse getIndexResponse = elasticsearchClient.indices().get(demo -> demo.index("*"));
        return String.join(",", getIndexResponse.result().keySet());
    }

    @GetMapping("deleteIndex")
    @ApiOperation(value = "删除", notes = "删除")
    public String deleteIndex() throws IOException {
        DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(demo -> demo.index("demo1"));
        return Boolean.TRUE.equals(deleteIndexResponse.acknowledged()) ? "删除成功" : "删除失败";
    }


}