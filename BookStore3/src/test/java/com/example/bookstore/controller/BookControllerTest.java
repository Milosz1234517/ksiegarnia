package com.example.bookstore.controller;

import com.example.bookstore.model.dto.bookDTO.BookHeaderDTO;
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
class BookControllerTest {

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
        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getBooksByTitle?page=1&title=" + bookHeader.getBookTitle()))
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
    @Transactional
    void getBooksFilter() throws Exception {
        //given
        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(0);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getBooksFilter?available=false&page=1&title=" + bookHeader.getBookTitle()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        BookHeaderDTO[] headerDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookHeaderDTO[].class);
        assertThat(headerDTOS).isNotNull();
        assertThat(headerDTOS[0].getBookTitle()).isEqualTo(bookHeader.getBookTitle());
        assertThat(headerDTOS[0].getQuantity()).isEqualTo(bookHeader.getQuantity());
        assertThat(headerDTOS.length).isEqualTo(1);
    }

    @Test
    @Transactional
    void getBooksFilterCount() throws Exception {
        //given
        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(0);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getBooksFilterCount?available=false&title=" + bookHeader.getBookTitle()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        Integer headerCount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Integer.class);
        assertThat(headerCount).isNotNull();
        assertThat(headerCount).isEqualTo(1);
    }

    @Test
    @Transactional
    void getBookWithDetails() throws Exception {
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
        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getBookWithDetails?bookHeaderId=" + bookHeader.getBookHeaderId()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        BookHeader header = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookHeader.class);
        assertThat(header).isNotNull();
        assertThat(header.getBookTitle()).isEqualTo(bookHeader.getBookTitle());
        assertThat(header.getQuantity()).isEqualTo(bookHeader.getQuantity());
        assertThat(header.getEdition()).isEqualTo(bookHeader.getEdition());
        assertThat(header.getPublishingHouse().getName()).isEqualTo(bookHeader.getPublishingHouse().getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addBook() throws Exception {
        //given
        //when
        MvcResult mvcResult = mockMvc.perform(post("/api/bookstore/addBook")
                        .content("""
                                {
                                  "bookAuthors": [
                                    {
                                      "name": "string",
                                      "surname": "string"
                                    }
                                  ],
                                  "bookCategories": [
                                    {
                                      "description": "string"
                                    }
                                  ],
                                  "bookTitle": "string",
                                  "description": "string",
                                  "edition": 1,
                                  "icon": "string",
                                  "price": 10,
                                  "publishingHouse": {
                                    "name": "string"
                                  },
                                  "quantity": 10,
                                  "releaseDate": "02-02-2000"
                                }"""))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        BookHeader header = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookHeader.class);
        assertThat(header).isNotNull();
        assertThat(header.getBookTitle()).isEqualTo("string");
        assertThat(header.getQuantity()).isEqualTo(10);
        assertThat(header.getEdition()).isEqualTo(1);
        assertThat(header.getPublishingHouse().getName()).isEqualTo("string");
    }

    @Test
    void updateBook() {
    }
}