package com.loeyae.springboot.demo.config;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
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
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ElasticsearchRestClientTest {

    @Autowired
    RestHighLevelClient highLevelClient;

    @Test
    void testCreateIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("zy-sample");
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("_doc");
                {
                    builder.startObject("properties");
                    {
                        builder.startObject("id");
                        {
                            builder.field("type", "long");
                        }
                        builder.endObject();
                        builder.startObject("count");
                        {
                            builder.field("type", "integer");
                        }
                        builder.endObject();
                        builder.startObject("price");
                        {
                            builder.field("type", "float");
                        }
                        builder.endObject();
                        builder.startObject("amount");
                        {
                            builder.field("type", "float");
                        }
                        builder.endObject();
                        builder.startObject("message");
                        {
                            builder.field("type", "text");
                            builder.field("analyzer", "chinese");
//                            builder.field("search_analyzer", "ik_smart");
                        }
                        builder.endObject();
                        builder.startObject("created");
                        {
                            builder.field("type", "long");
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createIndexRequest.mapping("_doc", builder);
        CreateIndexResponse createIndexdResponse = null;
        boolean acknowledged = false;
        boolean shardsAcknowledged = false;
        RestStatus restStatus = null;
        try {
            createIndexdResponse = highLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            acknowledged = createIndexdResponse.isAcknowledged();
            shardsAcknowledged = createIndexdResponse.isShardsAcknowledged();
        } catch (ElasticsearchException exc) {
            exc.printStackTrace();
            restStatus = exc.status();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(acknowledged);
        assertTrue(shardsAcknowledged);
        assertNull(restStatus);
    }

    @Test
    void testBulkInsert() {
        boolean ret = false;
        List<String> messages = Arrays.asList(new String[]{
                "这是测试数据", "测试es搜索", "es搜索数据", "备注消息", "全文检索示范", "搜索示例",
                "中文检索示例", "中文检索测试", "es中文检索", "es中文全文检索"
        });
        for (int i = 8656; i < 100000; i++) {
            BulkRequest bulkRequest = new BulkRequest();
            for (int j = 0; j < 10; j++) {
                Map<String, Object> map = new HashMap<>();
                Random random = new Random();
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.CHINA);
                nf.setMaximumFractionDigits(2);
                Integer c = random.nextInt(1000) + 1;
                BigDecimal p =
                        BigDecimal.valueOf(Double.valueOf(nf.format(random.nextFloat() * 100)));
                BigDecimal a = p.multiply(BigDecimal.valueOf(c));
                map.put("id", i * 10 + j + 1);
                map.put("count", c);
                map.put("price", p);
                map.put("amount", a);
                map.put("message", messages.get(new Random().nextInt(messages.size())));
                map.put("created", new Date().getTime());
                bulkRequest.add(new IndexRequest("zy-sample", "_doc"){{
                    source(map);
                }});
            }
            BulkResponse bulkResponse = null;
            try {
                bulkResponse = highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(bulkResponse.status());
            if (RestStatus.OK == bulkResponse.status()) {
                ret = true;
            }
        }
        assertTrue(ret);
    }

    @Test
    void testDeleteIndex() {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("zy-sample");
        AcknowledgedResponse deleteIndexResponse = null;
        RestStatus restStatus = null;
        boolean acknowledged = false;
        try {
            deleteIndexResponse = highLevelClient.indices().delete(deleteIndexRequest,
                    RequestOptions.DEFAULT);
            acknowledged = deleteIndexResponse.isAcknowledged();
        } catch (ElasticsearchException exc) {
            restStatus = exc.status();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        assertFalse(acknowledged);
//        assertEquals(RestStatus.NOT_FOUND, restStatus);
        assertTrue(acknowledged);
        assertNull(restStatus);
    }

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
        IndexRequest indexRequest = new IndexRequest("zy-test","order", "ZY-TEST");
        Map<String, Object> map = new HashMap<>();
        map.put("id", 10000);
        map.put("commodityId", 10);
        map.put("commodityName", "测试菜品10");
        map.put("storeId", 5);
        map.put("storeName", "测试商户5");
        map.put("price", BigDecimal.valueOf(5.50));
        map.put("amount", BigDecimal.valueOf(45.50));
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
        GetRequest getRequest = new GetRequest("zy-test", "order", "ZY-TEST");
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
        assertEquals("ZY-TEST", getResponse.getId());
        assertEquals(10000, getResponse.getSourceAsMap().get("id"));
        assertEquals(10, getResponse.getSourceAsMap().get("commodityId"));
        assertEquals(5, getResponse.getSourceAsMap().get("storeId"));
        assertEquals("测试商户5", getResponse.getSourceAsMap().get("commodityName"));
        assertEquals("测试菜品10", getResponse.getSourceAsMap().get("storeName"));
    }

    @Test
    void testUpdate() {
        UpdateRequest updateRequest = new UpdateRequest("zy-test", "order", "ZY-TEST");
        Map<String, Object> map = new HashMap<>();
        map.put("commodityId", 11);
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
    }

    @Test
    void testSearch() {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(new MatchAllQueryBuilder());
        sourceBuilder.size(1);
        searchRequest.source(sourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMillis(1L));
        SearchResponse searchResponse = null;
        try {
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(searchResponse);
        assertTrue(RestStatus.OK == searchResponse.status());
        List<Map<String, Object>> result = new ArrayList<>();
        searchResponse.getHits().iterator().forEachRemaining(item -> {
            result.add(item.getSourceAsMap());
        });
        assertTrue(result.size() > 0);
        SearchScrollRequest scrollRequest = new SearchScrollRequest();
        scrollRequest.scrollId(searchResponse.getScrollId());
        SearchResponse searchResponse1 = null;
        try {
            searchResponse1 = highLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(searchResponse1);
        assertTrue(RestStatus.OK == searchResponse1.status());
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        if (null != searchResponse.getScrollId()) {
            clearScrollRequest.addScrollId(searchResponse.getScrollId());
        }
        if (null != searchResponse1.getScrollId()) {
            clearScrollRequest.addScrollId(searchResponse1.getScrollId());
        }
        List<String> scollids = clearScrollRequest.getScrollIds();
        if (null != scollids && scollids.size() > 0) {
            ClearScrollResponse clearScrollResponse = null;
            try {
                clearScrollResponse = highLevelClient.clearScroll(clearScrollRequest,
                        RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertTrue(RestStatus.OK == clearScrollResponse.status());
            assertTrue(clearScrollResponse.isSucceeded());
        }
    }

    @Test
    void testDelete() {
        DeleteRequest deleteRequest = new DeleteRequest("zy-test", "order", "ZY-TEST");
        DeleteResponse deleteResponse = null;
        RestStatus restStatus = null;
        try {
            deleteResponse = highLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            restStatus = deleteResponse.status();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(deleteResponse);
        assertEquals(RestStatus.OK, restStatus);
        assertEquals(DocWriteResponse.Result.DELETED, deleteResponse.getResult());
    }
}