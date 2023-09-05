package com.github.aivachkin.socialmedia.dto.post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {

    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private List<String> imageLinks;
}
