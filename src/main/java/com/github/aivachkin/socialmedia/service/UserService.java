package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.dto.user.CreateUserRequest;
import com.github.aivachkin.socialmedia.dto.user.CreateUserResponse;
import com.github.aivachkin.socialmedia.entity.User;
import com.github.aivachkin.socialmedia.exception.UserAlreadyExistsException;
import com.github.aivachkin.socialmedia.mapper.UserMapper;
import com.github.aivachkin.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с сущностью User
 *
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    /**
     * Создание пользователя при регистрации
     *
     * @param createUserRequest ДТО с входящими данными пользователя
     * @return ДТО с ответом после создания и сохранения пользователя в БД
     */
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {

        if (userRepository.findByUsername(createUserRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким логином уже зарегистрирован");
        }

        User user = userMapper.toUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        userRepository.save(user);

        return userMapper.toCreateUserResponse(user);
    }

}
