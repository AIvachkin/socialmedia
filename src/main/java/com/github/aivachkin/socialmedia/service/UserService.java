package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.domain.JwtUser;
import com.github.aivachkin.socialmedia.dto.user.CreateUserRequest;
import com.github.aivachkin.socialmedia.dto.user.CreateUserResponse;
import com.github.aivachkin.socialmedia.dto.user.FriendshipDto;
import com.github.aivachkin.socialmedia.entity.*;
import com.github.aivachkin.socialmedia.exception.SubscriptionDoesNotExistException;
import com.github.aivachkin.socialmedia.exception.UserAlreadyExistsException;
import com.github.aivachkin.socialmedia.exception.UserCanNotSubscribeException;
import com.github.aivachkin.socialmedia.exception.UserNotFoundException;
import com.github.aivachkin.socialmedia.mapper.UserMapper;
import com.github.aivachkin.socialmedia.repository.SubscriptionRepository;
import com.github.aivachkin.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Сервис для работы с сущностью User
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    private final SubscriptionRepository subscriptionRepository;

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


    /**
     * Метод для отправки запроса другому пользователю на подписку
     *
     * @param friendshipDto ДТО, содержащий id адресата
     */
    public void sendFriendshipRequest(FriendshipDto friendshipDto) {

        User subscriber = userRepository.findById(getAuthenticatedUserId()).orElseThrow(
                () -> new UserNotFoundException("Пользователь, отправивший запрос, не найден"));

        User targetUser = userRepository.findById(friendshipDto.getTargetUserId()).orElseThrow(
                () -> new UserNotFoundException("Пользователь, которому отправлен запрос на подписку, не найден"));

        if (subscriber.getId().equals(targetUser.getId())) {
            throw new UserCanNotSubscribeException("пользователь не может подписаться сам на себя");
        }

        Subscription subscription = new Subscription()
                .setSubscriber(subscriber)
                .setTargetUser(targetUser)
                .setFriendStatus(FriendStatus.UNACCEPTED)
                .setSubStatus(SubStatus.USER1);

        subscriptionRepository.save(subscription);
    }

    /**
     * Метод для подтверждения взаимной подписки (переход в статус "дружба")
     *
     * @param friendshipDto ДТО, содержащий id отправителя запроса на дружбу
     */
    public void acceptFriendshipRequest(FriendshipDto friendshipDto) {


        Subscription subscription = subscriptionRepository.findBySubscriber_IdAndTargetUser_IdAndFriendStatus(
                friendshipDto.getTargetUserId(), getAuthenticatedUserId(), FriendStatus.UNACCEPTED).orElseThrow(
                () -> new SubscriptionDoesNotExistException("Подписка невозможна")
        );


        subscription.setFriendStatus(FriendStatus.ACCEPTED);
        subscription.setSubStatus(SubStatus.BOTH);

        subscriptionRepository.save(subscription);
    }

    /**
     * Метод для отклонения входящего запроса на подписку от другого пользователя
     *
     * @param friendshipDto ДТО - запрос на корректировку статуса подписки
     */
    public void declineFriendshipRequest(FriendshipDto friendshipDto) {

        Subscription subscription = subscriptionRepository.findBySubscriber_IdAndTargetUser_IdAndFriendStatus(
                friendshipDto.getTargetUserId(), getAuthenticatedUserId(), FriendStatus.UNACCEPTED).orElseThrow(
                () -> new SubscriptionDoesNotExistException("Подписка невозможна")
        );

        subscription.setFriendStatus(FriendStatus.DECLINE);
        subscriptionRepository.save(subscription);
    }


    /**
     * Метод для удаления подписчика из друзей (подписка удаленного пользователя на удалившего сохраняется)
     *
     * @param friendshipDto ДТО - запрос на корректировку статуса подписки
     */
    public void removeFriend(FriendshipDto friendshipDto) {

        Subscription subscription = subscriptionRepository.findBySubscriber_IdAndTargetUser_IdAndFriendStatus(
                getAuthenticatedUserId(), friendshipDto.getTargetUserId(), FriendStatus.ACCEPTED).orElseThrow(
                () -> new SubscriptionDoesNotExistException("Ошибочный запрос. Отписаться невозможно"));

        subscription.setFriendStatus(FriendStatus.UNACCEPTED);

        if (subscription.getSubscriber().getId().equals(getAuthenticatedUserId())) {
            subscription.setSubStatus(SubStatus.USER2);
        } else {
            subscription.setSubStatus(SubStatus.USER1);
        }

        subscriptionRepository.save(subscription);
    }


    /**
     * Метод для получения id пользователя из securityContext
     *
     * @return id текущего пользователя
     */
    protected Long getAuthenticatedUserId() {

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtUser.getUserId();
    }
}
