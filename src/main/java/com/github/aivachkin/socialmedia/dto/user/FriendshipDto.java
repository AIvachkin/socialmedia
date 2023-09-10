package com.github.aivachkin.socialmedia.dto.user;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ДТО - запрос на подписку (дружбу)
 *
 */
@Data
@Accessors(chain = true)
public class FriendshipDto {

    /**
     * id пользователя, которому отправлен запрос на подписку
     *
     */
    private Long targetUserId;
}
