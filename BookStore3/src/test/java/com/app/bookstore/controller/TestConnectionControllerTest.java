package com.app.bookstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestConnectionControllerTest {

    //given
    //when
    //then

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void userAccess() throws Exception {
        //given
        //when
        mockMvc.perform(get("/api/connection/user"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminAccess() throws Exception {
        mockMvc.perform(get("/api/connection/admin"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
    }
    @Test
    @WithMockUser(roles = "USER")
    void userNoAccess() throws Exception {
        //given
        //when
        mockMvc.perform(get("/api/connection/admin"))
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        //then
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminNoAccess() throws Exception {
        mockMvc.perform(get("/api/connection/user"))
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
    }
}