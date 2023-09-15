package com.github.aivachkin.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aivachkin.socialmedia.domain.JwtRequest;
import com.github.aivachkin.socialmedia.domain.JwtResponse;
import com.github.aivachkin.socialmedia.dto.user.FriendshipDto;
import com.github.aivachkin.socialmedia.entity.FriendStatus;
import com.github.aivachkin.socialmedia.entity.SubStatus;
import com.github.aivachkin.socialmedia.entity.Subscription;
import com.github.aivachkin.socialmedia.repository.SubscriptionRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "/sql/data-test-user.sql") // Путь к скрипту с тестовыми данными
class UserControllerTest {

    private static String accessToken;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    AuthService authService;

    @BeforeEach
    void prepareForTest() {
        getAccessToken();
    }

    @DisplayName("Тест для метода отправки запроса на дружбу")
    @Test
    @SneakyThrows
    void sendFriendshipRequestTest() {

        FriendshipDto friendshipDto = new FriendshipDto()
                .setTargetUserId(3L);

        long userId = 1L;

        mockMvc.perform(patch("/api/users/friendship/request")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(friendshipDto)))
                .andExpect(status().isOk());

        Optional<Subscription> savedRequest = subscriptionRepository.findByParam(
                userId, friendshipDto.getTargetUserId(), FriendStatus.UNACCEPTED, SubStatus.USER1);

        assertTrue(savedRequest.isPresent());
        assertEquals(userId, savedRequest.get().getSubscriber().getId());
        assertEquals(friendshipDto.getTargetUserId(), savedRequest.get().getTargetUser().getId());

    }

    @DisplayName("Тест для метода подтверждения дружбы")
    @Test
    @SneakyThrows
    void acceptFriendshipRequestTest() {

        FriendshipDto friendshipDto = new FriendshipDto()
                .setTargetUserId(2L);

        long userId = 1L;

        mockMvc.perform(patch("/api/users/friendship/accept")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(friendshipDto)))
                .andExpect(status().isOk());

        Optional<Subscription> acceptedRequest = subscriptionRepository.findByParam(
                friendshipDto.getTargetUserId(), userId, FriendStatus.ACCEPTED, SubStatus.BOTH);

        assertTrue(acceptedRequest.isPresent());
        assertEquals(friendshipDto.getTargetUserId(), acceptedRequest.get().getSubscriber().getId());
        assertEquals(userId, acceptedRequest.get().getTargetUser().getId());

    }

    @DisplayName("Тест для метода отклонения запроса на дружбу")
    @Test
    @SneakyThrows
    void declineFriendshipRequestTest() {

        FriendshipDto friendshipDto = new FriendshipDto()
                .setTargetUserId(2L);

        long userId = 1L;

        mockMvc.perform(patch("/api/users/friendship/reject")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(friendshipDto)))
                .andExpect(status().isOk());

        Optional<Subscription> declineRequest = subscriptionRepository.findByParam(
                friendshipDto.getTargetUserId(), userId, FriendStatus.DECLINE, SubStatus.USER2);

        assertTrue(declineRequest.isPresent());
        assertEquals(friendshipDto.getTargetUserId(), declineRequest.get().getSubscriber().getId());
        assertEquals(userId, declineRequest.get().getTargetUser().getId());

    }

    @DisplayName("Тест для метода удаления друга")
    @Test
    @SneakyThrows
    void removeFriendTest() {

        FriendshipDto friendshipDto = new FriendshipDto()
                .setTargetUserId(3L);

        long userId = 1L;

        mockMvc.perform(patch("/api/users/friendship/unfriend")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(friendshipDto)))
                .andExpect(status().isOk());

        Optional<Subscription> declineRequest = subscriptionRepository.findByParam(
                userId, friendshipDto.getTargetUserId(), FriendStatus.UNACCEPTED, SubStatus.USER2);

        assertTrue(declineRequest.isPresent());
        assertEquals(userId, declineRequest.get().getSubscriber().getId());
        assertEquals(friendshipDto.getTargetUserId(), declineRequest.get().getTargetUser().getId());
    }

    @SneakyThrows
    private void getAccessToken() {
        JwtRequest authRequest = new JwtRequest("user1@example.com", "password1");
        JwtResponse jwtResponse = authService.login(authRequest);
        accessToken = jwtResponse.getAccessToken();
    }
}