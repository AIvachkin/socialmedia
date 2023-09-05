package com.github.aivachkin.socialmedia.dto.user;


import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;

/**
 * ДТО - запрос на создание пользователя
 */
@Data
@Accessors(chain = true)
public class CreateUserRequest {

    /**
     * Логин пользователя
     */
    @NotEmpty(message = "Логин обязателен для ввода")
    private String username;

    /**
     * Пароль пользователя
     */
    @NotEmpty(message = "Пароль обязателен для ввода")
    private String password;

    /**
     * Имя пользователя
     */
    @NotEmpty(message = "Имя обязательно для ввода")
    private String firstName;

    /**
     * Фамилия пользователя
     */
    @NotEmpty(message = "Фамилия обязательна для ввода")
    private String lastName;
}
