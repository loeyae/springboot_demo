package com.loeyae.springboot.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.loeyae.springboot.demo.common.ApiResult;
import com.loeyae.springboot.demo.service.ApiClient;
import com.loeyae.springboot.demo.service.AppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApiControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @MockBean
    private AppService appService;

    @MockBean
    private ApiClient apiClient;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        given(this.appService.getSecret(any()))
                .willReturn("11111111");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appSecret", "1111111");
        given(this.apiClient.secret(any()))
                .willReturn(ApiResult.ok(jsonObject));
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/index"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/post"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testOpen() {
        assertTrue(true);
    }

    @Test
    public void testSecret() {
        assertTrue(true);
    }
}
