package com.app.bookstore.controller;

import com.app.bookstore.repository.AuthorRepository;
import com.app.bookstore.repository.BookHeaderRepository;
import com.app.bookstore.repository.CategoryRepository;
import com.app.bookstore.repository.PublishingHouseRepository;
import com.app.bookstore.model.dto.bookDTO.BookHeaderDTO;
import com.app.bookstore.model.entities.Author;
import com.app.bookstore.model.entities.BookHeader;
import com.app.bookstore.model.entities.Category;
import com.app.bookstore.model.entities.PublishingHouse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private ObjectMapper objectMapper;
    @Autowired
    private BookHeaderRepository bookHeaderRepository;
    @Autowired
    private PublishingHouseRepository publishingHouseRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CategoryRepository categoryRepository;

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
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void addBook() throws Exception {
        //given
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult mvcResult = mockMvc.perform(post("/api/bookstore/addBook")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
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
                                  "releaseDate": "2000-02-02"
                                }"""))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        String answer = mvcResult.getResponse().getContentAsString();
        assertThat(answer).isNotNull();
        assertThat(answer).isEqualTo("{\"message\":\"Book added successfully\"}");

        List<BookHeader> header = bookHeaderRepository.findAll();
        assertThat(header.size()).isEqualTo(1);
        assertThat(header.get(0).getBookTitle()).isEqualTo("string");
        assertThat(header.get(0).getQuantity()).isEqualTo(10);
        assertThat(header.get(0).getEdition()).isEqualTo(1);
        assertThat(header.get(0).getPublishingHouse().getName()).isEqualTo("string");
    }

    @Test
    @Transactional
    @WithMockUser(roles = "USER")
    void addBookWrongRole() throws Exception {
        //given
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult mvcResult = mockMvc.perform(post("/api/bookstore/addBook")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
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
                                  "releaseDate": "2000-02-02"
                                }"""))
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        //then
        String answer = mvcResult.getResponse().getContentAsString();
        assertThat(answer).isNotNull();
        assertThat(answer).isEqualTo("{\"message\":\"Something went wrong, no access to resources\"}");

        List<BookHeader> header = bookHeaderRepository.findAll();
        assertThat(header.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void updateBook() throws Exception {
        //given
        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        Author author = new Author();
        author.setName("name");
        author.setSurname("surname");

        Category category = new Category();
        category.setDescription("cat");

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(1);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setBookAuthors(new ArrayList<>(List.of(author)));
        bookHeader.setBookCategories(new ArrayList<>(List.of(category)));
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        publishingHouseRepository.save(publishingHouse);
        authorRepository.save(author);
        categoryRepository.save(category);
        bookHeaderRepository.save(bookHeader);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult mvcResult = mockMvc.perform(put("/api/bookstore/updateBook")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
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
                                  "bookHeaderId" :\040""" + bookHeader.getBookHeaderId() + ","+
                                """
                                  "description": "string",
                                  "edition": 10,
                                  "icon": "string",
                                  "price": 10,
                                  "publishingHouse": {
                                    "name": "string"
                                  },
                                  "quantity": 10,
                                  "releaseDate": "2000-02-02"
                                }"""))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        String answer = mvcResult.getResponse().getContentAsString();
        assertThat(answer).isNotNull();
        assertThat(answer).isEqualTo("{\"message\":\"Book updated successfully\"}");

        List<BookHeader> header = bookHeaderRepository.findAll();
        assertThat(header).isNotNull();
        assertThat(header.size()).isEqualTo(1);
        assertThat(header.get(0).getBookTitle()).isEqualTo("string");
        assertThat(header.get(0).getQuantity()).isEqualTo(10);
        assertThat(header.get(0).getEdition()).isEqualTo(10);
        assertThat(header.get(0).getPublishingHouse().getName()).isEqualTo("string");

        bookHeaderRepository.deleteAll();
        authorRepository.deleteAll();
        publishingHouseRepository.deleteAll();
        categoryRepository.deleteAll();

    }
}