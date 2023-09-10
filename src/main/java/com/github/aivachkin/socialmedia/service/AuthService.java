package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.domain.JwtAuthentication;
import com.github.aivachkin.socialmedia.domain.JwtRequest;
import com.github.aivachkin.socialmedia.domain.JwtResponse;
import com.github.aivachkin.socialmedia.entity.User;
import com.github.aivachkin.socialmedia.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;

    // Лучше хранить не в мапе, а использовать постоянное хранилище (например, Redis)
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;


    private final PasswordEncoder passwordEncoder;


    /**
     * Метод для возврата ответа с токенами в случае успешной идентификации пользователя по логину и паролю
     *
     * @param authRequest входящий запрос - введенные логин и пароль
     * @return ответ, содержащий токены
     * @throws AuthException исключение - если пользователь с таким логином/паролем не найден в базе
     */
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {

        final User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));


        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {

            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);

        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    /**
     * Метод для получения нового access токена на основании refresh токена
     *
     * @param refreshToken рефреш-токен пользователя
     * @return новый access токен
     * @throws AuthException исключение - если пользователь с таким логином/паролем не найден в базе
     */
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByUsername(username)
//                        userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }


    /**
     * Метод для получения нового набора токенов
     *
     * @param refreshToken текущий refresh токен
     * @return новый набор токенов - access и refresh
     * @throws AuthException исключение - если пользователь с таким логином/паролем не найден в базе
     */
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByUsername(username)
//                        userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    /**
     * Метод для получения аутентификационной информации пользователя из SecurityContext
     *
     * @return аутентификационная информация пользователя из SecurityContext
     */
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
