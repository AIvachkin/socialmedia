package com.github.aivachkin.socialmedia.controller;

import com.github.aivachkin.socialmedia.domain.JwtRequest;
import com.github.aivachkin.socialmedia.domain.JwtResponse;
import com.github.aivachkin.socialmedia.domain.RefreshJwtRequest;
import com.github.aivachkin.socialmedia.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;

/**
 * Контроллер для работы с аутентификационными данными пользователя (вход в систему и получение токенов)
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Контроллер для получения токенов
     *
     * @param authRequest запрос, содержащий логин и пароль пользователя
     * @return ответ, содержащий токены
     * @throws AuthException исключение - если пользователь с таким логином/паролем не найден в базе
     */
    @Operation(
            summary = "Аутентификация в системе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Введены корректные логин и пароль, предоставлены access и refresh токены"
                    )
            }, tags = "Authentication")
    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * Контроллер для получения access токена
     *
     * @param request текущий refresh токен пользователя
     * @return access токен
     */
    @Operation(
            summary = "Получение access токена",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Access токен предоставлен"
                    )
            }, tags = "Authentication")
    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    /**
     * Контроллер для получения нового набора токенов
     *
     * @param request текущий refresh токен пользователя
     * @return набор токенов - access и refresh
     */
    @Operation(
            summary = "Получение access и refresh токенов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Access и refresh токены предоставлены"
                    )
            }, tags = "Authentication")
    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }


}
