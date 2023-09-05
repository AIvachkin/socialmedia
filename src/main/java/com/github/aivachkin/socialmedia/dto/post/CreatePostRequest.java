package com.github.aivachkin.socialmedia.dto.post;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;


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

    /**
     * Ссылки на изображения к посту
     */
    private List<String> imageLink;

}
