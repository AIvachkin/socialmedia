package com.github.aivachkin.socialmedia.controller;

import com.github.aivachkin.socialmedia.dto.message.MessageRequestDto;
import com.github.aivachkin.socialmedia.dto.message.MessageResponseDto;
import com.github.aivachkin.socialmedia.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с сообщениями пользователей
 */
@RestController
@RequestMapping("api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * Отправка сообщения пользователю, находящемуся в статусе друга
     *
     * @param messageRequestDto ДТО - запрос на отправку сообщения
     * @return ДТО - результат обработки запроса на отправку сообщения
     */
    @Operation(
            summary = "Отправка сообщения другому пользователю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Сообщение успешно отправлено"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Пользователь не может отправлять сообщение себе"),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Пользователь не является другом. Отправка сообщения невозможна")
            })
    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageRequestDto messageRequestDto) {

        return ResponseEntity.ok(messageService.sendMessage(messageRequestDto));
    }

}
