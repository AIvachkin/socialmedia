package com.github.aivachkin.socialmedia.dto.user;


import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class CreateUserRequest {

    @NotEmpty(message = "Логин обязателен для ввода")
    private String username;

    @NotEmpty(message = "Пароль обязателен для ввода")
    private String password;

    @NotEmpty(message = "Имя обязательно для ввода")
    private String firstName;

    @NotEmpty(message = "Фамилия обязательна для ввода")
    private String lastName;
}
