package com.loeyae.springboot.demo.controller

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner.class)
@WebMvcTest(IndexController.class)
class ApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testIndex() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/index"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPost() {
    }

    @Test
    public void testOpen() {
    }

    @Test
    public void testSecret() {
    }
}
