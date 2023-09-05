package com.github.aivachkin.socialmedia.dto.post;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePostResponse {

    private String title;
    private String text;

}
