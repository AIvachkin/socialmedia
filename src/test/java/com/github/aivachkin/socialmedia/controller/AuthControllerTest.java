package com.github.aivachkin.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aivachkin.socialmedia.domain.JwtRequest;
import com.github.aivachkin.socialmedia.domain.JwtResponse;
import com.github.aivachkin.socialmedia.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor
@Sql(scripts = "/sql/data-test-auth.sql") // Путь к скрипту с тестовыми данными
class AuthControllerTest {

    private static JwtResponse jwtResponse;

    @Autowired
    AuthService authService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup(TestInfo testInfo) {
        if (!testInfo.getTestMethod().get().getName().equals("loginTest")) {
            getTokens();
        }
    }

    @DisplayName("Тест для метода аутентификации и получения access и refresh токенов")
    @Test
    @SneakyThrows
    void loginTest() {

        JwtRequest authRequest = new JwtRequest("user1@example.com", "password");

        String jsonAuthRequest = new ObjectMapper().writeValueAsString(authRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAuthRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());

    }

    @DisplayName("Тест для метода получения нового access токена")
    @Test
    @SneakyThrows
    void getNewAccessTokenTest() {

        String refreshToken = jwtResponse.getRefreshToken();
        String jsonRefreshToken = new ObjectMapper().writeValueAsString(refreshToken);

        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").isEmpty());

    }

    @DisplayName("Тест для метода получения новых access и refresh токенов")
    @Test
    @SneakyThrows
    void getNewRefreshTokenTest() {

        String refreshToken = jwtResponse.getRefreshToken();
        String jsonRefreshToken = new ObjectMapper().writeValueAsString(refreshToken);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRefreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());

    }


    @SneakyThrows
    private void getTokens() {
        JwtRequest authRequest = new JwtRequest("user1@example.com", "password");
        jwtResponse = authService.login(authRequest);
    }
}