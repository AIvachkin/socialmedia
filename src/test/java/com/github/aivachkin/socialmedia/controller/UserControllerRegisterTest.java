package com.github.aivachkin.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aivachkin.socialmedia.dto.user.CreateUserRequest;
import com.github.aivachkin.socialmedia.entity.User;
import com.github.aivachkin.socialmedia.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerRegisterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @DisplayName("Тест для метода по регистрации и созданию нового пользователя")
    @Test
    @SneakyThrows
    void registerTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .setUsername("user1@example.com")
                .setPassword("password")
                .setFirstName("user1")
                .setLastName("userov");

        String jsonCreateUser = new ObjectMapper().writeValueAsString(createUserRequest);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateUser))
                .andExpect(status().isOk());

        Optional<User> savedUser = userRepository.findByUsername("user1@example.com");
        assertTrue(savedUser.isPresent());
    }
}
