package com.github.aivachkin.socialmedia.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс - сущность запроса для получения JWT-токена
 */
@Getter
@Setter
@AllArgsConstructor
public class JwtRequest {

    /**
     * Логин пользователя
     */
    private String username;

    /**
     * Пароль пользователя
     */
    private String password;

}
