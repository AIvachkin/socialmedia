package com.github.aivachkin.socialmedia.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ДТО - ответ, полученный после отправки сообщения пользователю
 *
 */
@Data
@Accessors(chain = true)
public class MessageResponseDto {

    /**
     * id отправителя сообщения
     *
     */
    private Long senderId;

    /**
     * id получателя сообщения
     *
     */
    private Long receiverId;

    /**
     * содержание сообщения
     *
     */
    private String text;

}
