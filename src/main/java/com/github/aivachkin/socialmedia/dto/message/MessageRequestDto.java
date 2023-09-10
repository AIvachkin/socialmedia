package com.github.aivachkin.socialmedia.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ДТО - запрос на отправку сообщения пользователя
 *
 */
@Data
@Accessors(chain = true)
public class MessageRequestDto {

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
