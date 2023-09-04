package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.entity.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Метод возвращает пользователя по логину. Пользователи заранее создаются в конструкторе
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final List<User> users;

    public UserService() {
        this.users = List.of(
                new User(1L,"anton", "1234", "Антон", "Иванов"),
                new User(2L,"ivan", "12345", "Сергей", "Петров")
        );
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return users.stream()
                .filter(user -> login.equals(user.getUsername()))
                .findFirst();
    }
}
