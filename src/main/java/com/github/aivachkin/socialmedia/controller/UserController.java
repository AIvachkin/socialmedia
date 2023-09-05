package com.github.aivachkin.socialmedia.controller;


import com.github.aivachkin.socialmedia.dto.user.CreateUserRequest;
import com.github.aivachkin.socialmedia.dto.user.CreateUserResponse;
import com.github.aivachkin.socialmedia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Контроллер для работы с сущностями пользователей
 *
 */
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Регистрация нового пользователя
     *
     * @param request ДТО, содержащий необходимую информацию для создания пользователя
     * @return статус создания пользователя и ответ в виде ДТО
     */
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Необходимо указать логин, пароль, имя и фамилию",
    responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь зарегистрирован и внесен в БД"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь с таким логином уже существует"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Некорректный формат вводных данных")
    },tags = "User")
    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@Valid
                                                       @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }


}

