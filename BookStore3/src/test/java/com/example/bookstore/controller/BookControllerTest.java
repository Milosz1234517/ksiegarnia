package com.example.bookstore.controller;

import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.PublishingHouse;
import com.example.bookstore.repository.BookHeaderRepository;
import com.example.bookstore.repository.PublishingHouseRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class BookControllerTest {

    //given
    //when
    //then

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private BookHeaderRepository bookHeaderRepository;
    @Autowired
    private PublishingHouseRepository publishingHouseRepository;


    @Test
    @Transactional
    void searchBooksByTitle() throws Exception {
        //given
        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(1);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getBooksByTitle?page=1&title="+bookHeader.getBookTitle()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        String[] title = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), String[].class);
        assertThat(title).isNotNull();
        assertThat(title[0]).isEqualTo(bookHeader.getBookTitle().toLowerCase());
        assertThat(title.length).isEqualTo(1);
    }

    @Test
    void getBooksFilter() {
    }

    @Test
    void getBooksFilterCount() {
    }

    @Test
    void getBookWithDetails() {
    }

    @Test
    void getBooksByCategory() {
    }

    @Test
    void addBook() {
    }

    @Test
    void updateBook() {
    }
}