package com.github.aivachkin.socialmedia.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс - ответ на запрос пользователя на получение JWT-токена -
 * access и refresh токены
 */
@Getter
@AllArgsConstructor
public class JwtResponse {

    /**
     * Тип токена
     */
    private final String type = "Bearer";

    /**
     * access токен
     */
    private String accessToken;

    /**
     * refresh токен
     */
    private String refreshToken;

}
