package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Сервис для генерации и валидации access и refresh токенов
 */
@Component
@Slf4j
public class JwtProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtProvider(
            @Value("${jwt.secret.access}")
            String jwtAccessSecret,

            @Value("${jwt.secret.refresh}")
            String jwtRefreshSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    /**
     * Метод для генерации access токена
     *
     * @param user объект пользователя
     * @return access токен
     */
    public String generateAccessToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("roles", user.getRoles())
                .claim("firstName", user.getFirstName())
                .compact();
    }

    /**
     * Метод для генерации refresh токена
     *
     * @param user объект пользователя
     * @return refresh токен
     */
    public String generateRefreshToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    /**
     * Метод для валидации access токена
     *
     * @param accessToken проверяемый токен
     * @return результат валидации (true или false)
     */
    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    /**
     * Метод для валидации refresh токена
     *
     * @param refreshToken проверяемый токен
     * @return результат валидации (true или false)
     */
    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    /**
     * Метод для валидации токена
     *
     * @param token проверяемый токен
     * @param secret секретный ключ токена
     * @return результат валидации (true или false)
     */
    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Срок действия токена истек", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Неподдерживаемый jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Некорректный jwt", mjEx);
        } catch (Exception e) {
            log.error("Недействительный токен", e);
        }
        return false;
    }

    /**
     * Метод для извлечения полезных данных из токена
     *
     * @param token токен пользователя
     * @return полезные данные пользователя из токена
     */
    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    /**
     * Метод для получения claims (логина) из refresh токена
     *
     * @param token текущий токен
     * @return claims - полезные данные токена
     */
    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    /**
     * Метод для получения claims (заявок) из токена
     *
     * @param token текущий токен
     * @param secret секретный ключ токена
     * @return claims - полезные данные из токена
     */
    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
