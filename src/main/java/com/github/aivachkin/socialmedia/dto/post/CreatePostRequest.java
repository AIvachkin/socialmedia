package com.github.aivachkin.socialmedia.dto.post;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * ДТО - запрос на создание поста
 *
 */
@Data
@Accessors(chain = true)
public class CreatePostRequest {

    /**
     * Заголовок поста
     */
    @NotEmpty(message = "Заголовок обязателен для ввода")
    private String title;

    /**
     * Содержание поста
     */
    @NotEmpty(message = "Текст обязателен для ввода")
    private String text;


}
