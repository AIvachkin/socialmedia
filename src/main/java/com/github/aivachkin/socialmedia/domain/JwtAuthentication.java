package com.github.aivachkin.socialmedia.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Класс - аутентификационные данные пользователя
 *
 */
@Getter
@Setter
public class JwtAuthentication implements Authentication {

    /**
     * Флаг аутентификации (true - пользователь аутентифицирован, false - не аутентифицирован)
     */
    private boolean authenticated;

    /**
     * Логин пользователя
     */
    private String username;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * id пользователя
     */
    private Long userId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return new JwtUser(userId, username);
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Метод для установки флага аутентификации пользователя
     *
     * @param isAuthenticated параметр для установки флага аутентификации (true или false)
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return firstName;
    }
}
