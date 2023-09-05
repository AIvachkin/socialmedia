package com.github.aivachkin.socialmedia.dto.post;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePostResponse {
    /**
     * Заголовок поста
     */
    private String title;

    /**
     * Содержание поста
     */
    private String text;

}
