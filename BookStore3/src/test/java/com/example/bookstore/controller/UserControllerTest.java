package com.example.bookstore.controller;

import com.example.bookstore.model.entities.Users;
import com.example.bookstore.model.entities.role.ERole;
import com.example.bookstore.model.entities.role.Role;
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

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Test
    @Transactional
    void changePassword() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Users users = new Users();
        users.setLogin("user");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("user"));
        users.setRoles(new HashSet<>(Set.of(role)));

        roleRepository.save(role);
        userRepository.save(users);
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

        mockMvc
                .perform(put("/api/bookstore/changePassword?oldPass=user&newPass=Test@1234")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        mockMvc.perform(post("/api/auth/login")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "user",
                                    "password": "Test@1234"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    @Transactional
    void changeUserDetails() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Users users = new Users();
        users.setLogin("user@user.pl");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("user"));
        users.setRoles(new HashSet<>(Set.of(role)));

        roleRepository.save(role);
        userRepository.save(users);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "user@user.pl",
                                    "password": "user"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        mockMvc
                .perform(put("/api/bookstore/changeUserDetails")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                  "login": "user@user.pl",
                                  "name": "string",
                                  "phoneNumber": "111111111",
                                  "surname": "string"
                                }""")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        Users users1 = userRepository.findByLogin(users.getLogin()).orElseThrow();
        assertThat(users1).isNotNull();
        assertThat(users1.getName()).isEqualTo("string");
        assertThat(users1.getSurname()).isEqualTo("string");
        assertThat(users1.getPhoneNumber()).isEqualTo(111111111);
    }

    @Test
    @Transactional
    void getUsersFilter() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        Users users = new Users();
        users.setLogin("user@user.pl");
        users.setName("string");
        users.setSurname("string");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("user"));
        users.setRoles(new HashSet<>(Set.of(role)));

        roleRepository.save(role);
        userRepository.save(users);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/api/auth/loginAdmin")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "user@user.pl",
                                    "password": "user"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        MvcResult mvcResult = mockMvc
                .perform(get("/api/bookstore/getUsersFilter?page=1")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        List<Users> users1 = userRepository.findAll();
        assertThat(users1.size()).isEqualTo(1);
        assertThat(users1.get(0).getName()).isEqualTo("string");
        assertThat(users1.get(0).getSurname()).isEqualTo("string");
        assertThat(users1.get(0).getPhoneNumber()).isEqualTo(123456789);
    }

    @Test
    @Transactional
    void getUserDetails() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Users users = new Users();
        users.setLogin("user@user.pl");
        users.setName("string");
        users.setSurname("string");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("user"));
        users.setRoles(new HashSet<>(Set.of(role)));

        roleRepository.save(role);
        userRepository.save(users);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "user@user.pl",
                                    "password": "user"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        MvcResult mvcResult = mockMvc
                .perform(get("/api/bookstore/getUserDetails")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //then
        Users users1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Users.class);
        assertThat(users1).isNotNull();
        assertThat(users1.getName()).isEqualTo("string");
        assertThat(users1.getSurname()).isEqualTo("string");
        assertThat(users1.getPhoneNumber()).isEqualTo(123456789);
    }
}