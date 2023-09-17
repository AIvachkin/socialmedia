package com.github.aivachkin.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aivachkin.socialmedia.domain.JwtRequest;
import com.github.aivachkin.socialmedia.domain.JwtResponse;
import com.github.aivachkin.socialmedia.dto.post.CreatePostRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void createPost() {

        CreatePostRequest createPostRequest = new CreatePostRequest()
                .setTitle("Title")
                .setText("Text");

        String jsonPost = new ObjectMapper().writeValueAsString(createPostRequest);

        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPost))
                .andExpect(status().isOk());

        Optional<Post> savedPost1 = postRepository.findById(1L);
        Optional<Post> savedPost2 = postRepository.findById(2L);
        assertTrue(savedPost1.isPresent());
        assertTrue(savedPost2.isPresent());
        assertEquals("Title", savedPost1.get().getTitle());
        assertEquals("Title", savedPost2.get().getTitle());
        assertEquals("Text", savedPost1.get().getText());
        assertEquals("Text", savedPost2.get().getText());

    }

    @Test
    void updatePostImage() {
    }

    @Test
    void getPostByUserId() {
    }

    @Test
    void updatePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void getUserActivityFeed() {
    }

    @SneakyThrows
    private void getAccessToken() {
        JwtRequest jwtRequest = new JwtRequest("user1@example.com", "password");
        JwtResponse jwtResponse = authService.login(jwtRequest);
        accessToken = jwtResponse.getAccessToken();
    }

}