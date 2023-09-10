package com.github.aivachkin.socialmedia.dto.post;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ДТО - запрос на обновление поста
 *
 */
@Data
@Accessors(chain = true)
public class UpdatePostDto {

    /**
     * Заголовок поста
     */
    private String title;

    /**
     * Содержание поста
     */
    private String text;

}
