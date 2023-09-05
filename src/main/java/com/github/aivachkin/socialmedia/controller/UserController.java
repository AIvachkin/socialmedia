package com.github.aivachkin.socialmedia.controller;


import com.github.aivachkin.socialmedia.dto.user.CreateUserRequest;
import com.github.aivachkin.socialmedia.dto.user.CreateUserResponse;
import com.github.aivachkin.socialmedia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Создание пользователя
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Необходимо указать логин, пароль, имя и фамилию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован и внесен в БД"),
            @ApiResponse(responseCode = "400", description = "Пользователь с таким логином уже существует"),
            @ApiResponse(responseCode = "409", description = "Некорректный формат вводных данных")
    })
    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@Valid
                                                       @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }


}

