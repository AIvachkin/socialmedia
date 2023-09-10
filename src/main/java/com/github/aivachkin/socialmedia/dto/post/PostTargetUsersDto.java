package com.github.aivachkin.socialmedia.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ДТО - отображение постов пользователей
 *
 */
@Data
@Accessors(chain = true)
public class PostTargetUsersDto {

    /**
     * Поле - список постов пользователей
     */
    @JsonProperty("results")
    private List<PostDto> postsDtoList;

}
