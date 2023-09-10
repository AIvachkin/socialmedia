package com.github.aivachkin.socialmedia.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {

    /**
     * refresh токен
     */
    public String refreshToken;

}
