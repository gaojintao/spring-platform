package com.example.spring.es.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.example.spring.es.entity.Poet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EsDocController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/22 11:04
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("es/doc/")
@RequiredArgsConstructor
@Api(tags = "es 文档操作", consumes = "application/json", produces = "application/json", protocols = "http")

public class EsDocController {
    private final ElasticsearchClient elasticsearchClient;
    private static String INDEX_NAME = "index_home";
    @GetMapping("createDoc")
    @ApiOperation(value = "创建文档", notes = "创建文档")
    public void createDoc() throws IOException {
        Map<String, Object> doc = new HashMap<>();
        doc.put("age", 30);
        doc.put("name", "李白");
        doc.put("poems", "静夜思");
        doc.put("about", "字太白");
        doc.put("success", "创造了古代浪漫主义文学高峰、歌行体和七绝达到后人难及的高度");

        CreateResponse response = elasticsearchClient.create(builder -> builder.index(INDEX_NAME).id("1").document(doc));
        log.info(response.toString());

        Poet poet = new Poet(31, "杜甫", "登高", "字子美", "唐代伟大的现实主义文学作家，唐诗思想艺术的集大成者");
        response = elasticsearchClient.create(builder -> builder.index(INDEX_NAME).id("2").document(poet));
        log.info(response.toString());
    }
    @GetMapping("deleteDoc")
    @ApiOperation(value = "删除文档", notes = "删除文档")
    public void deleteDoc() throws IOException {
        DeleteResponse response = elasticsearchClient.delete(builder -> builder.index(INDEX_NAME).id("1"));
        log.info(response.toString());
    }
    @GetMapping("updateDoc")
    @ApiOperation(value = "修改文档", notes = "修改文档")
    public void updateDoc() throws IOException {
        Map<String, Object> doc = new HashMap<>();
        doc.put("age", 33);
        doc.put("name", "李白2");

        UpdateResponse response = elasticsearchClient.update(builder -> builder.index(INDEX_NAME).id("1").doc(doc), Map.class);
        log.info(response.toString());

        Poet poet = new Poet();
        poet.setAge(40);
        poet.setName("杜甫2");
        response = elasticsearchClient.update(builder -> builder.index(INDEX_NAME).id("2").doc(poet).docAsUpsert(true), Poet.class);
        log.info(response.toString());
    }
    @GetMapping("createOrUpdateDoc")
    @ApiOperation(value = "新增或修改文档", notes = "新增或修改文档")
    public void createOrUpdateDoc() throws IOException {
        Map<String, Object> doc = new HashMap<>();
        doc.put("age", 33);
        doc.put("name", "李白2");

        //只更新设置的字段
        IndexResponse response = elasticsearchClient.index(builder -> builder.index(INDEX_NAME).id("1").document(doc));
        log.info(response.toString());

        Poet poet = new Poet();
        poet.setAge(40);
        poet.setName("杜甫2");
        response = elasticsearchClient.index(builder -> builder.index(INDEX_NAME).id("2").document(poet));
        log.info(response.toString());
    }

    @GetMapping("bulk")
    @ApiOperation(value = "批量操作", notes = "批量操作")
    public void bulk() throws IOException {
        List<BulkOperation> list = new ArrayList<>();

        //批量新增
        for (int i = 0; i < 5; i++) {
            Map<String, Object> doc = new HashMap<>();
            doc.put("age", 30);
            doc.put("name", "李白" + i);
            doc.put("poems", "静夜思");
            doc.put("about", "字太白");
            doc.put("success", "创造了古代浪漫主义文学高峰、歌行体和七绝达到后人难及的高度");
            String id = 10 + i + "";
            list.add(new BulkOperation.Builder().create(builder -> builder.index(INDEX_NAME).id(id).document(doc)).build());
        }
        for (int i = 0; i < 5; i++) {
            Poet poet = new Poet(31, "杜甫" + i, "登高", "字子美", "唐代伟大的现实主义文学作家，唐诗思想艺术的集大成者");
            String id = 20 + i + "";
            list.add(new BulkOperation.Builder().create(builder -> builder.index(INDEX_NAME).id(id).document(poet)).build());
        }

        //批量删除
        list.add(new BulkOperation.Builder().delete(builder -> builder.index(INDEX_NAME).id("1")).build());
        list.add(new BulkOperation.Builder().delete(builder -> builder.index(INDEX_NAME).id("2")).build());

        BulkResponse response = elasticsearchClient.bulk(builder -> builder.index(INDEX_NAME).operations(list));
        log.info(response.toString());
    }
}
