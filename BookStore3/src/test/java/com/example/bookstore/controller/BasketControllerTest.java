package com.example.bookstore.controller;

import com.example.bookstore.model.entities.Basket;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.PublishingHouse;
import com.example.bookstore.model.entities.Users;
import com.example.bookstore.model.entities.role.ERole;
import com.example.bookstore.model.entities.role.Role;
import com.example.bookstore.payload.response.BasketTotalResponse;
import com.example.bookstore.payload.response.JwtResponse;
import com.example.bookstore.repository.*;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BookHeaderRepository bookHeaderRepository;
    @Autowired
    private PublishingHouseRepository publishingHouseRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    BasketRepository basketRepository;

    @Test
    @Transactional
    void getBasket() throws Exception {
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
        bookHeader.setQuantity(1);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        Basket basket = new Basket();
        basket.setUser(users);
        basket.setBookHeader(bookHeader);
        basket.setQuantity(1);
        basket.setPrice(BigDecimal.valueOf(7));

        roleRepository.save(role);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        basketRepository.save(basket);
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

        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getBasket")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        BasketTotalResponse basketTotalResponse = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BasketTotalResponse.class);
        assertThat(basketTotalResponse).isNotNull();
        assertThat(basketTotalResponse.getBasket().size()).isEqualTo(1);
        assertThat(basketTotalResponse.getTotalPrice()).isEqualTo(BigDecimal.valueOf(7));
    }

    @Test
    @Transactional
    void addItemToBasket() throws Exception {
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
        bookHeader.setQuantity(1);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        roleRepository.save(role);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
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

        MvcResult mvcResult = mockMvc.perform(post("/api/bookstore/addItemToBasket")
                .header("Authorization", response.getType() + " " + response.getAccessToken())
                .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                .content("{\"bookHeaderId\": " + bookHeader.getBookHeaderId() + "}"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Item added successfully\"}");

        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).isNotNull();
        assertThat(basketList.size()).isEqualTo(1);
        assertThat(basketList.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(7));
        assertThat(basketList.get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    @Transactional
    void updateItem() throws Exception {
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
        bookHeader.setQuantity(10);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        Basket basket = new Basket();
        basket.setUser(users);
        basket.setBookHeader(bookHeader);
        basket.setQuantity(1);
        basket.setPrice(BigDecimal.valueOf(7));

        roleRepository.save(role);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        basketRepository.save(basket);
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

        MvcResult mvcResult = mockMvc.perform(put("/api/bookstore/updateItem")
                        .header("Authorization", response.getType() + " " + response.getAccessToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("{\"bookHeader\": {\"bookHeaderId\":" + bookHeader.getBookHeaderId() +
                                "}, \"quantity\": 5}"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        BasketTotalResponse resp = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BasketTotalResponse.class);

        assertThat(resp).isNotNull();
        assertThat(resp.getBasket().size()).isEqualTo(1);
        assertThat(resp.getBasket().get(0).getPrice()).isEqualTo(BigDecimal.valueOf(35));
        assertThat(resp.getTotalPrice()).isEqualTo(BigDecimal.valueOf(35));
        assertThat(resp.getBasket().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    @Transactional
    void deleteItemFromBasket() throws Exception {
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
        bookHeader.setQuantity(10);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        Basket basket = new Basket();
        basket.setUser(users);
        basket.setBookHeader(bookHeader);
        basket.setQuantity(2);
        basket.setPrice(BigDecimal.valueOf(7));

        roleRepository.save(role);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        basketRepository.save(basket);
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

        mockMvc.perform(delete("/api/bookstore/deleteItemFromBasket?itemId="+basket.getItemId())
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<Basket> resp = basketRepository.findAll();

        assertThat(resp).isNotNull();
        assertThat(resp.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    void deleteAllItemsFromBasket() throws Exception {
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
        bookHeader.setQuantity(10);
        bookHeader.setIcon("");
        bookHeader.setBookTitle("Title test");
        bookHeader.setEdition(1);
        bookHeader.setPrice(BigDecimal.valueOf(7));
        bookHeader.setPublishingHouse(publishingHouse);
        bookHeader.setReleaseDate(Date.valueOf(LocalDate.now()));

        Basket basket = new Basket();
        basket.setUser(users);
        basket.setBookHeader(bookHeader);
        basket.setQuantity(2);
        basket.setPrice(BigDecimal.valueOf(7));

        roleRepository.save(role);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        basketRepository.save(basket);
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

        mockMvc.perform(delete("/api/bookstore/deleteAllItemsFromBasket")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<Basket> resp = basketRepository.findAll();

        assertThat(resp).isNotNull();
        assertThat(resp.size()).isEqualTo(0);
    }
}