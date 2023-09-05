package com.github.aivachkin.socialmedia.dto.post;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
public class CreatePostRequest {

    private String title;

    private String text;

    private List<String> imageLink;

}
