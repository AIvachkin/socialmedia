package com.github.aivachkin.socialmedia.controller;


import com.github.aivachkin.socialmedia.dto.user.CreateUserRequest;
import com.github.aivachkin.socialmedia.dto.user.CreateUserResponse;
import com.github.aivachkin.socialmedia.dto.user.FriendshipDto;
import com.github.aivachkin.socialmedia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Контроллер для работы с сущностями пользователей
 */
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Регистрация нового пользователя
     *
     * @param request ДТО, содержащий необходимую информацию для создания пользователя
     * @return статус создания пользователя и ответ в виде ДТО
     */
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Необходимо указать логин, пароль, имя и фамилию",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь зарегистрирован и внесен в БД"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Пользователь с таким логином уже существует"),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Некорректный формат вводных данных")
            }, tags = "User")
    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@Valid
                                                       @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }


    /**
     * Отправка запроса на подписку на другого пользователя
     *
     * @param friendshipDto ДТО - запрос на подписку (дружбу)
     */
    @Operation(
            summary = "Отправка запроса на подписку другому пользователю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос на подписку отправлен"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден в БД")

            }, tags = "User")
    @PatchMapping("/friendship/request")
    public void sendFriendshipRequest(@RequestBody FriendshipDto friendshipDto) {
        userService.sendFriendshipRequest(friendshipDto);
    }


    /**
     * Принятие запроса на подписку (подтверждение дружбы)
     *
     * @param friendshipDto ДТО - запрос на подписку (дружбу)
     */
    @Operation(
            summary = "Согласие на дружбу (принятие запроса)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос на дружбу принят"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден, подписка невозможна")
            }, tags = "User")
    @PatchMapping("/friendship/accept")
    public void acceptFriendshipRequest(@RequestBody FriendshipDto friendshipDto) {

        userService.acceptFriendshipRequest(friendshipDto);
    }


    /**
     * Отклонение входящего запроса на подписку от другого пользователя
     *
     * @param friendshipDto ДТО - запрос на корректировку статуса подписки
     */
    @Operation(
            summary = "Несогласие на взаимную подписку (отклонение запроса)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос на дружбу отклонен"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден")
            }, tags = "User")
    @PatchMapping("/friendship/reject")
    public void declineFriendshipRequest(@RequestBody FriendshipDto friendshipDto) {

        userService.declineFriendshipRequest(friendshipDto);
    }


    /**
     * Удаления подписчика из друзей (подписка удаленного пользователя на удалившего сохраняется)
     *
     * @param friendshipDto ДТО - запрос на корректировку статуса подписки
     */
    @Operation(
            summary = "Отказ от дружбы (подписки)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Подписка отменена"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден")
            }, tags = "User")
    @PatchMapping("/friendship/unfriend")
    public void removeFriend(@RequestBody FriendshipDto friendshipDto) {

        userService.removeFriend(friendshipDto);
    }




}

