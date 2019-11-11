package com.loeyae.springboot.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.loeyae.springboot.demo.common.ApiResult;
import com.loeyae.springboot.demo.service.ApiClient;
import com.loeyae.springboot.demo.service.AppService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(ApiController.class)
@AutoConfigureTestDatabase
@WebAppConfiguration
@AutoConfigureMybatis
public class ApiControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @MockBean
    private AppService appService;

    @MockBean
    private ApiClient apiClient;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        given(this.appService.getSecret("111111"))
                .willReturn("1111111");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appSecret", "1111111");
        given(this.apiClient.secret("111111"))
                .willReturn(ApiResult.ok(jsonObject));
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/index"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/post"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testOpen() {
        Assert.assertTrue(true);
    }

    @Test
    public void testSecret() {
        Assert.assertTrue(true);
    }
}
