package com.github.aivachkin.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aivachkin.socialmedia.domain.JwtRequest;
import com.github.aivachkin.socialmedia.domain.JwtResponse;
import com.github.aivachkin.socialmedia.dto.post.CreatePostRequest;
import com.github.aivachkin.socialmedia.dto.post.UpdatePostDto;
import com.github.aivachkin.socialmedia.entity.Post;
import com.github.aivachkin.socialmedia.repository.PostRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Sql(scripts = "/sql/data-test-post.sql") // Путь к скрипту с тестовыми данными
class PostControllerTest {

    private static String accessToken;


    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    AuthService authService;

    @BeforeEach
    void prepareForTest() {
        getAccessToken();
    }


    @DisplayName("Тест для метода создания поста")
    @Test
    @SneakyThrows
    void createPostTest() {

        CreatePostRequest createPostRequest = new CreatePostRequest()
                .setTitle("Title")
                .setText("Text");

        String jsonPost = new ObjectMapper().writeValueAsString(createPostRequest);

        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPost))
                .andExpect(status().isOk());

        Optional<Post> savedPost = postRepository.findById(2L);
        assertTrue(savedPost.isPresent());
        assertEquals("Title", savedPost.get().getTitle());
        assertEquals("Text", savedPost.get().getText());

    }

    @DisplayName("Тест для метода редактирования поста")
    @Test
    @SneakyThrows
    void updatePostTest() {

        long postId = 1L;

        UpdatePostDto updatePostDto = new UpdatePostDto()
                .setTitle("New Title")
                .setText("New Text");

        String jsonPostUpd = new ObjectMapper().writeValueAsString(updatePostDto);

        mockMvc.perform(patch("/api/posts/" + postId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPostUpd)
                        .flashAttr("updatePostDto", updatePostDto))
                .andExpect(status().isOk());

        Optional<Post> savedPost = postRepository.findById(postId);
        assertTrue(savedPost.isPresent());
        assertEquals("New Title", savedPost.get().getTitle());
        assertEquals("New Text", savedPost.get().getText());
    }

    @DisplayName("Тест для метода удаления поста пользователя")
    @Test
    @SneakyThrows
    void deletePostTest() {

        long postId = 1L;

        mockMvc.perform(delete("/api/posts/" + postId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @DisplayName("Тест для метода постраничного вывода постов пользователя")
    @Test
    @SneakyThrows
    void getPostByUserIdTest() {

        long userId = 1L;
        int offset = 0;
        int limit = 10;

        mockMvc.perform(get("/api/posts/" + userId + "?offset=" + offset + "&limit=" + limit)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

    }

    @DisplayName("Тест для метода просмотра ленты активности")
    @Test
    @SneakyThrows
    void getUserActivityFeed() {

        int offset = 0;
        int limit = 10;

        mockMvc.perform(get("/api/posts/activity-feed" + "?offset=" + offset + "&limit=" + limit)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

    }

    @SneakyThrows
    private void getAccessToken() {
        JwtRequest jwtRequest = new JwtRequest("user1@example.com", "password");
        JwtResponse jwtResponse = authService.login(jwtRequest);
        accessToken = jwtResponse.getAccessToken();
    }

}