package com.github.aivachkin.socialmedia.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(chain = true)
public class JwtUser {

    private final Long userId;
    private final String username;



}
