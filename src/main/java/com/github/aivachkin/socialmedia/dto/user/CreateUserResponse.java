package com.github.aivachkin.socialmedia.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ДТО - ответ, полученный после создания пользователя
 */
@Data
@Accessors(chain = true)
public class CreateUserResponse {

    /**
     * Логин пользователя
     */
    private String username;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;
}
