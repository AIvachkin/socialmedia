package com.github.aivachkin.socialmedia.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс - сущность запроса для получения JWT-токена
 */
@Getter
@Setter
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
