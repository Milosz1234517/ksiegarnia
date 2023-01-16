package com.app.bookstore.controller;

import com.app.bookstore.model.entities.*;
import com.app.bookstore.repository.*;
import com.app.bookstore.model.entities.role.ERole;
import com.app.bookstore.model.entities.role.Role;
import com.app.bookstore.payload.response.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookHeaderRepository bookHeaderRepository;
    @Autowired
    private PublishingHouseRepository publishingHouseRepository;
    @Autowired
    private BookReviewsRepository bookReviewsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private OrderHeaderRepository orderHeaderRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Test
    @Transactional
    void getReviewsForBook() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Users users = new Users();
        users.setLogin("user");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("user"));
        users.setRoles(Set.of(role));
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

        BookReviews bookReviews = new BookReviews();
        bookReviews.setBookHeader(bookHeader);
        bookReviews.setMark(10);
        bookReviews.setApproveStatus(true);
        bookReviews.setUser(users);

        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        roleRepository.save(role);
        userRepository.save(users);
        bookReviewsRepository.save(bookReviews);
        //when
        MvcResult mvcResult = mockMvc
                .perform(get("/api/bookstore/getReviewsForBook?page=1&bookHeaderId=" + bookHeader.getBookHeaderId()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        BookReviews[] reviews = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookReviews[].class);
        assertThat(reviews).isNotNull();
        assertThat(reviews[0].getReviewId()).isEqualTo(bookReviews.getReviewId());
        assertThat(reviews[0].getBookHeader().getBookTitle()).isEqualTo(bookReviews.getBookHeader().getBookTitle());
        assertThat(reviews.length).isEqualTo(1);
    }

    @Test
    @Transactional
    void getReviewsForUser() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Users users = new Users();
        users.setLogin("user");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("user"));
        users.setRoles(Set.of(role));

        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        Users users1 = new Users();
        users1.setLogin("user1");
        users1.setName("");
        users1.setSurname("");
        users1.setPhoneNumber(123456789);
        users1.setPassword(encoder.encode("user1"));

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(0);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        BookReviews bookReviews = new BookReviews();
        bookReviews.setBookHeader(bookHeader);
        bookReviews.setMark(10);
        bookReviews.setApproveStatus(true);
        bookReviews.setUser(users);

        BookReviews bookReviews1 = new BookReviews();
        bookReviews1.setBookHeader(bookHeader);
        bookReviews1.setMark(10);
        bookReviews1.setApproveStatus(true);
        bookReviews1.setUser(users1);

        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        roleRepository.save(role);
        userRepository.save(users);
        userRepository.save(users1);
        bookReviewsRepository.save(bookReviews);
        bookReviewsRepository.save(bookReviews1);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "user",
                                    "password": "user"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        MvcResult mvcResult = mockMvc
                .perform(get("/api/bookstore/getReviewsForUser?page=1")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        BookReviews[] reviews = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookReviews[].class);
        assertThat(reviews).isNotNull();
        assertThat(reviews[0].getReviewId()).isEqualTo(bookReviews.getReviewId());
        assertThat(reviews[0].getBookHeader().getBookTitle()).isEqualTo(bookReviews.getBookHeader().getBookTitle());
        assertThat(reviews.length).isEqualTo(1);
    }

    @Test
    @Transactional
    void getReviewsForApprove() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Role role1 = new Role();
        role1.setName(ERole.ROLE_ADMIN);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
        users.setRoles(Set.of(role1));

        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        Users users1 = new Users();
        users1.setLogin("user1");
        users1.setName("");
        users1.setSurname("");
        users1.setPhoneNumber(123456789);
        users1.setPassword(encoder.encode("user1"));
        users1.setRoles(Set.of(role));

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(0);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        BookReviews bookReviews = new BookReviews();
        bookReviews.setBookHeader(bookHeader);
        bookReviews.setMark(10);
        bookReviews.setApproveStatus(false);
        bookReviews.setUser(users1);

        BookReviews bookReviews1 = new BookReviews();
        bookReviews1.setBookHeader(bookHeader);
        bookReviews1.setMark(10);
        bookReviews1.setApproveStatus(true);
        bookReviews1.setUser(users1);

        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        roleRepository.save(role);
        roleRepository.save(role1);
        userRepository.save(users);
        userRepository.save(users1);
        bookReviewsRepository.save(bookReviews);
        bookReviewsRepository.save(bookReviews1);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/api/auth/loginAdmin")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "admin",
                                    "password": "admin"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        MvcResult mvcResult = mockMvc
                .perform(get("/api/bookstore/getReviewsForApprove?page=1")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        BookReviews[] reviews = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookReviews[].class);
        assertThat(reviews).isNotNull();
        assertThat(reviews[0].getReviewId()).isEqualTo(bookReviews.getReviewId());
        assertThat(reviews[0].getBookHeader().getBookTitle()).isEqualTo(bookReviews.getBookHeader().getBookTitle());
        assertThat(reviews.length).isEqualTo(1);
    }

    @Test
    @Transactional
    void reviewBook() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        OrderStatus status = new OrderStatus();
        status.setStatusId(3);
        status.setDescription("finalized test");

        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        Users users1 = new Users();
        users1.setLogin("user1");
        users1.setName("");
        users1.setSurname("");
        users1.setPhoneNumber(123456789);
        users1.setPassword(encoder.encode("user1"));
        users1.setRoles(Set.of(role));

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(0);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setOrderStatus(status);
        orderHeader.setUser(users1);
        orderHeader.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderHeader.setTotalPrice(BigDecimal.valueOf(10));

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderHeader(orderHeader);
        orderItems.setBookHeader(bookHeader);
        orderItems.setQuantity(1);
        orderItems.setPrice(BigDecimal.valueOf(7));

        orderHeader.setOrderItems(new ArrayList<>(List.of(orderItems)));
        BookReviews bookReviews = new BookReviews();
        bookReviews.setBookHeader(bookHeader);
        bookReviews.setMark(10);
        bookReviews.setApproveStatus(false);
        bookReviews.setUser(users1);

        BookReviews bookReviews1 = new BookReviews();
        bookReviews1.setBookHeader(bookHeader);
        bookReviews1.setMark(10);
        bookReviews1.setApproveStatus(true);
        bookReviews1.setUser(users1);

        orderStatusRepository.save(status);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        roleRepository.save(role);
        userRepository.save(users1);
        orderItemsRepository.save(orderItems);
        orderHeaderRepository.save(orderHeader);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "user1",
                                    "password": "user1"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        mockMvc
                .perform(post("/api/bookstore/reviewBook")
                        .header("Authorization", response.getType() + " " + response.getAccessToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                {
                                  "bookHeaderId": %s,
                                  "description": "string",
                                  "mark": 10
                                }""", bookHeader.getBookHeaderId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<BookReviews> reviews = bookReviewsRepository.findAll();
        assertThat(reviews.get(0).getMark()).isEqualTo(10);
        assertThat(reviews.get(0).getBookHeader().getBookTitle()).isEqualTo(bookReviews.getBookHeader().getBookTitle());
        assertThat(reviews.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void approveReview() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Role role1 = new Role();
        role1.setName(ERole.ROLE_ADMIN);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
        users.setRoles(Set.of(role1));

        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        Users users1 = new Users();
        users1.setLogin("user1");
        users1.setName("");
        users1.setSurname("");
        users1.setPhoneNumber(123456789);
        users1.setPassword(encoder.encode("user1"));
        users1.setRoles(Set.of(role));

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(0);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        BookReviews bookReviews = new BookReviews();
        bookReviews.setBookHeader(bookHeader);
        bookReviews.setMark(10);
        bookReviews.setApproveStatus(false);
        bookReviews.setUser(users1);

        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        roleRepository.save(role);
        roleRepository.save(role1);
        userRepository.save(users);
        userRepository.save(users1);
        bookReviewsRepository.save(bookReviews);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/api/auth/loginAdmin")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "admin",
                                    "password": "admin"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        mockMvc
                .perform(put("/api/bookstore/approveReview?reviewId=" + bookReviews.getReviewId())
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<BookReviews> reviews = bookReviewsRepository.findAll();
        assertThat(reviews.get(0).getMark()).isEqualTo(10);
        assertThat(reviews.get(0).isApproveStatus()).isEqualTo(true);
        assertThat(reviews.get(0).getBookHeader().getBookTitle()).isEqualTo(bookReviews.getBookHeader().getBookTitle());
        assertThat(reviews.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void deleteReview() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Role role1 = new Role();
        role1.setName(ERole.ROLE_ADMIN);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
        users.setRoles(Set.of(role1));

        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setName("pub");

        Users users1 = new Users();
        users1.setLogin("user1");
        users1.setName("");
        users1.setSurname("");
        users1.setPhoneNumber(123456789);
        users1.setPassword(encoder.encode("user1"));
        users1.setRoles(Set.of(role));

        BookHeader bookHeader = new BookHeader();
        bookHeader.setQuantity(0);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        BookReviews bookReviews = new BookReviews();
        bookReviews.setBookHeader(bookHeader);
        bookReviews.setMark(10);
        bookReviews.setApproveStatus(true);
        bookReviews.setUser(users1);

        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        roleRepository.save(role);
        roleRepository.save(role1);
        userRepository.save(users);
        userRepository.save(users1);
        bookReviewsRepository.save(bookReviews);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/api/auth/loginAdmin")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "admin",
                                    "password": "admin"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        mockMvc
                .perform(delete("/api/bookstore/deleteReview?reviewId=" + bookReviews.getReviewId())
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<BookReviews> reviews = bookReviewsRepository.findAll();
        assertThat(reviews.size()).isEqualTo(0);
    }
}