package com.github.aivachkin.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aivachkin.socialmedia.domain.JwtRequest;
import com.github.aivachkin.socialmedia.domain.JwtResponse;
import com.github.aivachkin.socialmedia.dto.message.MessageResponseDto;
import com.github.aivachkin.socialmedia.entity.Message;
import com.github.aivachkin.socialmedia.repository.MessageRepository;
import com.github.aivachkin.socialmedia.service.AuthService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Sql(scripts = "/sql/data-test-message.sql") // Путь к скрипту с тестовыми данными
class MessageControllerTest {

    private static String accessToken;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthService authService;

    @Autowired
    MessageRepository messageRepository;

    @BeforeEach
    void prepareForTest() {
        getAccessToken();
    }


    @DisplayName("Тест для метода отправки сообщения")
    @Test
    @SneakyThrows
    void sendMessageTest() {

        MessageResponseDto messageResponseDto = new MessageResponseDto()
                .setReceiverId(3L)
                .setText("Text");

        long userId = 1L;

        String jsonMessage = new ObjectMapper().writeValueAsString(messageResponseDto);

        mockMvc.perform(post("/api/messages")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMessage))
                .andExpect(status().isOk());

        List<Message> messages = messageRepository.findBySender_IdAndReceiver_Id(userId, messageResponseDto.getReceiverId());
        assertFalse(messages.isEmpty());
        assertEquals(messageResponseDto.getText(),messages.get(0).getText());

    }


    @SneakyThrows
    private void getAccessToken() {
        JwtRequest jwtRequest = new JwtRequest("user1@example.com", "password");
        JwtResponse jwtResponse = authService.login(jwtRequest);
        accessToken = jwtResponse.getAccessToken();
    }

}