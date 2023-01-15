package com.example.bookstore.controller;

import com.example.bookstore.model.dto.bookDTO.BookHeaderDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.model.entities.PublishingHouse;
import com.example.bookstore.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    void getCategories() throws Exception {
        //given
        Category category = new Category();
        category.setDescription("category");

        categoryRepository.save(category);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getCategories"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        Category[] categories = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Category[].class);
        assertThat(categories).isNotNull();
        assertThat(categories[0].getDescription()).isEqualTo(category.getDescription());
        assertThat(categories.length).isEqualTo(1);
    }
}