package com.github.aivachkin.socialmedia.utility;

import com.github.aivachkin.socialmedia.domain.JwtAuthentication;
import com.github.aivachkin.socialmedia.entity.Role;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс - утилита для получения данных из текущего токена пользователя
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtility {

    /**
     * Метод для генерации аутентификационных данных пользователя (информационный токен)
     *
     * @param claims полезные данные для формирования токена
     * @return информационный токен, сформированный на основе полученных данных пользователя
     */
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setUserId(claims.get("userId", Long.class));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());

        return jwtInfoToken;
    }

    /**
     * Метод для получения роли из полезных данных токена (пока не используется - для дальнейшего расширения функционала)
     *
     * @param claims полезные данные токена
     * @return роль пользователя
     */
    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}
