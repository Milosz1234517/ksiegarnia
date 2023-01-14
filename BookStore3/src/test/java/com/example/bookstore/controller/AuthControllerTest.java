package com.example.bookstore.controller;

import com.example.bookstore.model.entities.Users;
import com.example.bookstore.model.entities.role.ERole;
import com.example.bookstore.model.entities.role.Role;
import com.example.bookstore.payload.response.JwtResponse;
import com.example.bookstore.repository.RoleRepository;
import com.example.bookstore.repository.UserRepository;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void authenticateUser() throws Exception {
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
        //then
        mockMvc.perform(get("/api/connection/user")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    @Transactional
    void authenticateAdmin() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        Users users = new Users();
        users.setLogin("admin");
        users.setName("");
        users.setSurname("");
        users.setPhoneNumber(123456789);
        users.setPassword(encoder.encode("admin"));
        users.setRoles(Set.of(role));

        roleRepository.save(role);
        userRepository.save(users);
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
        //then
        mockMvc.perform(get("/api/connection/admin")
                        .header("Authorization", response.getType() + " " + response.getAccessToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    @Transactional
    void registerUser() throws Exception {
        //given
        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        roleRepository.save(role);
        //when
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        mockMvc.perform(post("/api/auth/register")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                  "name": "string",
                                  "password": "String@1234",
                                  "phone": 123456789,
                                  "sname": "string",
                                  "username": "string@string.pl"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/register")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                  "name": "string",
                                  "password": "String@1234",
                                  "phone": 123456789,
                                  "sname": "string",
                                  "username": "string@string.pl"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        //then
        String userTaken = mvcResult.getResponse().getContentAsString();
        assertThat(userTaken).isEqualTo("{\"message\":\"Error: Username is already taken!\"}");

        mockMvc.perform(post("/api/auth/login")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "username": "string@string.pl",
                                    "password": "String@1234"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
    }
}