package com.github.aivachkin.socialmedia.service;


import com.github.aivachkin.socialmedia.dto.message.MessageRequestDto;
import com.github.aivachkin.socialmedia.dto.message.MessageResponseDto;
import com.github.aivachkin.socialmedia.entity.Message;
import com.github.aivachkin.socialmedia.entity.SubStatus;
import com.github.aivachkin.socialmedia.entity.User;
import com.github.aivachkin.socialmedia.exception.UserCanNotWriteException;
import com.github.aivachkin.socialmedia.exception.UserNotFoundException;
import com.github.aivachkin.socialmedia.exception.UsersAreNotFriendsException;
import com.github.aivachkin.socialmedia.mapper.MessageMapper;
import com.github.aivachkin.socialmedia.repository.MessageRepository;
import com.github.aivachkin.socialmedia.repository.SubscriptionRepository;
import com.github.aivachkin.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с сообщениями пользователей
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserService userService;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Метод для отправки сообщения пользователю
     *
     * @param messageRequestDto ДТО - запрос на отправку сообщения
     * @return ДТО - отправленное и сохраненное в БД сообщение
     */
    public MessageResponseDto sendMessage(MessageRequestDto messageRequestDto) {

        List<User> users = sendAbilityCheck(userService.getAuthenticatedUserId(), messageRequestDto.getReceiverId());

        Message message = new Message()
                .setSender(users.get(0))
                .setReceiver(users.get(1))
                .setText(messageRequestDto.getText())
                .setSendingTime(LocalDateTime.now());

        messageRepository.save(message);

        return messageMapper.toMessageResponseDto(message);
    }

    /**
     * Метод для проверки возможности отправки сообщения пользователю
     *
     * @param userId1 id отправителя
     * @param userId2 id адресата
     * @return список пользователей из БД
     */
    private List<User> sendAbilityCheck(Long userId1, Long userId2) {

        if (userId1.equals(userId2)) {
            throw new UserCanNotWriteException("Пользователь не может отправлять сообщение себе");
        }

        if (subscriptionRepository.findSubscriptionsWithSubStatus(
                userId1, userId2, SubStatus.BOTH) == null) {
            throw new UsersAreNotFriendsException("Пользователи не являются друзьями");
        }

        List<User> users = new ArrayList<>();
        User sender = userRepository.findById(userId1).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id " + userId1 + " не найден"));
        User receiver = userRepository.findById(userId2).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id " + userId2 + " не найден"));
        users.add(sender);
        users.add(receiver);

        return users;

    }
}
