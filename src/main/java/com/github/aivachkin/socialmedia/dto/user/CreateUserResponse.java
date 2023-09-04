package com.github.aivachkin.socialmedia.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateUserResponse {

    private String username;

    private String firstName;

    private String lastName;
}
