package com.github.aivachkin.socialmedia.dto.post;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * ДТО - стандартное представление поста
 *
 */
@Data
@Accessors(chain = true)
public class PostDto {

    private Long id;
    private Long userId;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private String imageLink;

}
