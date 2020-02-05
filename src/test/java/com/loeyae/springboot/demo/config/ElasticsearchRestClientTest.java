package com.loeyae.springboot.demo.config;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ElasticsearchRestClientTest {

    @Autowired
    RestHighLevelClient highLevelClient;

    @Test
    void testCount() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("zy-test");
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchQuery(
                "commodityName", "测试菜品")));

        long total = 0L;
        SearchResponse searchResponse = null;
        try {
            searchResponse = highLevelClient.search(searchRequest,
                    RequestOptions.DEFAULT);
            total = searchResponse.getHits().getTotalHits();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(searchResponse);
        assertEquals(2, total);
    }

    @Test
    void testInsert() {
        IndexRequest indexRequest = new IndexRequest("zy-test","order");
        Map<String, Object> map = new HashMap<>();
        map.put("id", 3);
        map.put("commodityId", 3);
        map.put("commodityName", "测试菜品3");
        map.put("storeId", 1);
        map.put("storeName", "测试商户");
        map.put("price", BigDecimal.valueOf(5.00));
        map.put("amount", BigDecimal.valueOf(101.00));
        map.put("createTime", LocalDateTime.now());
        indexRequest.source(map);
        IndexResponse indexResponse = null;
        RestStatus restStatus = null;
        try {
            indexResponse = highLevelClient.index(indexRequest,
                    RequestOptions.DEFAULT);
            restStatus = indexResponse.status();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(indexResponse);
        assertEquals(RestStatus.CREATED, restStatus);
    }

    @Test
    void testGet() {
        GetRequest getRequest = new GetRequest("zy-test", "order", "AfXLFHABtZ2-QHd9XYL6");
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true);
//        fetchSourceContext.excludes();
        getRequest.fetchSourceContext(fetchSourceContext);
        GetResponse getResponse = null;
        try {
            getResponse = highLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(getResponse);
        assertEquals("zy-test", getResponse.getIndex());
        assertEquals("order", getResponse.getType());
        assertEquals("AfXLFHABtZ2-QHd9XYL6", getResponse.getId());
        assertEquals(1, getResponse.getSourceAsMap().get("id"));
        assertEquals(1, getResponse.getSourceAsMap().get("commodityId"));
        assertEquals(1, getResponse.getSourceAsMap().get("storeId"));
        assertEquals("测试商户", getResponse.getSourceAsMap().get("commodityName"));
        assertEquals("测试菜品1", getResponse.getSourceAsMap().get("storeName"));
    }

    @Test
    void testUpdate() {
        UpdateRequest updateRequest = new UpdateRequest("zy-test", "order", "AvUwFXABtZ2-QHd9QoJ9");
        Map<String, Object> map = new HashMap<>();
        map.put("commodityId", 2);
        updateRequest.doc(map);
        UpdateResponse updateResponse = null;
        RestStatus restStatus = null;
        try {
            updateResponse = highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            restStatus = updateResponse.status();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(updateResponse);
        assertEquals(RestStatus.OK, restStatus);
        assertEquals(DocWriteResponse.Result.UPDATED, updateResponse.getResult());
        UpdateRequest updateRequest1 = new UpdateRequest("zy-test", "order", "AvUwFXABtZ2" +
                "-QHd9QoJ9");
        Map<String, Object> map1 = new HashMap<>();
        map.put("commodityId", 2);
        updateRequest1.doc(map1);
        try {
            highLevelClient.update(updateRequest1, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}