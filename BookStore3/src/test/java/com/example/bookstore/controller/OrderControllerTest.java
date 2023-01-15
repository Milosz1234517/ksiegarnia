package com.example.bookstore.controller;

import com.example.bookstore.model.entities.*;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

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
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private OrderHeaderRepository orderHeaderRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Test
    @Transactional
    void placeOrder() throws Exception {
        //given
        OrderStatus status = new OrderStatus();
        status.setStatusId(1);
        status.setDescription("placed test");

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

        orderStatusRepository.save(status);
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

        MvcResult mvcResult = mockMvc.perform(post("/api/bookstore/placeOrder")
                        .header("Authorization", response.getType() + " " + response.getAccessToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                {
                                  "description": "string",
                                  "orderItems": [
                                    {
                                      "bookHeader": {
                                        "bookHeaderId": %s
                                      },
                                      "quantity": 1
                                    }
                                  ]
                                }""", bookHeader.getBookHeaderId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Order placed successfully\"}");

        List<OrderHeader> orderHeaderList = orderHeaderRepository.findAll();
        assertThat(orderHeaderList.size()).isEqualTo(1);
        assertThat(orderHeaderList.get(0).getOrderItems().size()).isEqualTo(1);
        assertThat(orderHeaderList.get(0).getOrderStatus().getDescription()).isEqualTo(status.getDescription());
        assertThat(orderHeaderList.get(0).getTotalPrice()).isEqualTo(BigDecimal.valueOf(7));
    }

    @Test
    @Transactional
    void cancelOrder() throws Exception {
        //given
        OrderStatus status = new OrderStatus();
        status.setStatusId(1);
        status.setDescription("placed test");

        OrderStatus status1 = new OrderStatus();
        status1.setStatusId(2);
        status1.setDescription("canceled test");

        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
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

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setOrderStatus(status);
        orderHeader.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderHeader.setTotalPrice(BigDecimal.valueOf(10));

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderHeader(orderHeader);
        orderItems.setBookHeader(bookHeader);
        orderItems.setQuantity(1);
        orderItems.setPrice(BigDecimal.valueOf(7));

        orderHeader.setOrderItems(new ArrayList<>(List.of(orderItems)));

        orderStatusRepository.save(status);
        orderStatusRepository.save(status1);
        roleRepository.save(role);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        orderItemsRepository.save(orderItems);
        orderHeaderRepository.save(orderHeader);
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

        mockMvc.perform(put("/api/bookstore/cancelOrder?orderId=" + orderHeader.getOrderId())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<OrderHeader> orderHeaderList = orderHeaderRepository.findAll();
        assertThat(orderHeaderList.size()).isEqualTo(1);
        assertThat(orderHeaderList.get(0).getOrderStatus().getDescription()).isEqualTo(status1.getDescription());
        assertThat(orderHeaderList.get(0).getOrderStatus().getStatusId()).isEqualTo(2);
    }

    @Test
    @Transactional
    void finalizeOrder() throws Exception {
        //given
        OrderStatus status = new OrderStatus();
        status.setStatusId(4);
        status.setDescription("reserved test");

        OrderStatus status1 = new OrderStatus();
        status1.setStatusId(3);
        status1.setDescription("finalized test");

        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
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

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setOrderStatus(status);
        orderHeader.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderHeader.setTotalPrice(BigDecimal.valueOf(10));

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderHeader(orderHeader);
        orderItems.setBookHeader(bookHeader);
        orderItems.setQuantity(1);
        orderItems.setPrice(BigDecimal.valueOf(7));

        orderHeader.setOrderItems(new ArrayList<>(List.of(orderItems)));

        orderStatusRepository.save(status);
        orderStatusRepository.save(status1);
        roleRepository.save(role);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        orderItemsRepository.save(orderItems);
        orderHeaderRepository.save(orderHeader);
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

        mockMvc.perform(put("/api/bookstore/finalizeOrder?orderId=" + orderHeader.getOrderId())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<OrderHeader> orderHeaderList = orderHeaderRepository.findAll();
        assertThat(orderHeaderList.size()).isEqualTo(1);
        assertThat(orderHeaderList.get(0).getOrderStatus().getDescription()).isEqualTo(status1.getDescription());
        assertThat(orderHeaderList.get(0).getOrderStatus().getStatusId()).isEqualTo(3);
    }

    @Test
    @Transactional
    void bookOrder() throws Exception {
        //given
        OrderStatus status = new OrderStatus();
        status.setStatusId(1);
        status.setDescription("placed test");

        OrderStatus status1 = new OrderStatus();
        status1.setStatusId(4);
        status1.setDescription("canceled test");

        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
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

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setOrderStatus(status);
        orderHeader.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderHeader.setTotalPrice(BigDecimal.valueOf(10));

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderHeader(orderHeader);
        orderItems.setBookHeader(bookHeader);
        orderItems.setQuantity(1);
        orderItems.setPrice(BigDecimal.valueOf(7));

        orderHeader.setOrderItems(new ArrayList<>(List.of(orderItems)));

        orderStatusRepository.save(status);
        orderStatusRepository.save(status1);
        roleRepository.save(role);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        orderItemsRepository.save(orderItems);
        orderHeaderRepository.save(orderHeader);
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

        mockMvc.perform(put("/api/bookstore/bookOrder?orderId=" + orderHeader.getOrderId())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<OrderHeader> orderHeaderList = orderHeaderRepository.findAll();
        assertThat(orderHeaderList.size()).isEqualTo(1);
        assertThat(orderHeaderList.get(0).getOrderStatus().getDescription()).isEqualTo(status1.getDescription());
        assertThat(orderHeaderList.get(0).getOrderStatus().getStatusId()).isEqualTo(4);
    }

    @Test
    @Transactional
    void getOrdersFilterAdmin() throws Exception {
        //given
        OrderStatus status = new OrderStatus();
        status.setStatusId(1);
        status.setDescription("placed test");

        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        Role role1 = new Role();
        role1.setName(ERole.ROLE_USER);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
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

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setOrderStatus(status);
        orderHeader.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderHeader.setTotalPrice(BigDecimal.valueOf(10));

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderHeader(orderHeader);
        orderItems.setBookHeader(bookHeader);
        orderItems.setQuantity(1);
        orderItems.setPrice(BigDecimal.valueOf(7));

        orderHeader.setOrderItems(new ArrayList<>(List.of(orderItems)));

        orderStatusRepository.save(status);
        roleRepository.save(role);
        roleRepository.save(role1);
        userRepository.save(users);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        orderItemsRepository.save(orderItems);
        orderHeaderRepository.save(orderHeader);
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

        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getOrdersFilterAdmin?page=1")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        OrderHeader[] orderHeaderList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderHeader[].class);
        assertThat(orderHeaderList).isNotNull();
        assertThat(orderHeaderList[0].getOrderId()).isEqualTo(orderHeader.getOrderId());
        assertThat(orderHeaderList.length).isEqualTo(1);
    }

    @Test
    @Transactional
    void getOrdersFilterUser() throws Exception {
        //given
        OrderStatus status = new OrderStatus();
        status.setStatusId(1);
        status.setDescription("placed test");

        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        Role role1 = new Role();
        role1.setName(ERole.ROLE_USER);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
        users.setRoles(Set.of(role));

        Users users1 = new Users();
        users1.setLogin("user");
        users1.setName("");
        users1.setSurname("");
        users1.setPhoneNumber(123456789);
        users1.setPassword(encoder.encode("user"));
        users1.setRoles(Set.of(role1));

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

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setOrderStatus(status);
        orderHeader.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderHeader.setTotalPrice(BigDecimal.valueOf(10));
        orderHeader.setUser(users1);

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderHeader(orderHeader);
        orderItems.setBookHeader(bookHeader);
        orderItems.setQuantity(1);
        orderItems.setPrice(BigDecimal.valueOf(7));

        OrderHeader orderHeader1 = new OrderHeader();
        orderHeader1.setOrderStatus(status);
        orderHeader1.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderHeader1.setTotalPrice(BigDecimal.valueOf(10));
        orderHeader1.setUser(users);

        OrderItems orderItems1 = new OrderItems();
        orderItems1.setOrderHeader(orderHeader);
        orderItems1.setBookHeader(bookHeader);
        orderItems1.setQuantity(1);
        orderItems1.setPrice(BigDecimal.valueOf(7));

        orderHeader.setOrderItems(new ArrayList<>(List.of(orderItems)));

        orderStatusRepository.save(status);
        roleRepository.save(role);
        roleRepository.save(role1);
        userRepository.save(users);
        userRepository.save(users1);
        publishingHouseRepository.save(publishingHouse);
        bookHeaderRepository.save(bookHeader);
        orderItemsRepository.save(orderItems);
        orderHeaderRepository.save(orderHeader);
        orderItemsRepository.save(orderItems1);
        orderHeaderRepository.save(orderHeader1);
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

        MvcResult mvcResult = mockMvc.perform(get("/api/bookstore/getOrdersFilterUser?page=1")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        OrderHeader[] orderHeaderList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderHeader[].class);
        assertThat(orderHeaderList).isNotNull();
        assertThat(orderHeaderList[0].getOrderId()).isEqualTo(orderHeader.getOrderId());
        assertThat(orderHeaderList.length).isEqualTo(1);
    }
}