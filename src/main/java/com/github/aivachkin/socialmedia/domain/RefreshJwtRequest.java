package com.github.aivachkin.socialmedia.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshJwtRequest {

    /**
     * refresh токен
     */
    public String refreshToken;

}
