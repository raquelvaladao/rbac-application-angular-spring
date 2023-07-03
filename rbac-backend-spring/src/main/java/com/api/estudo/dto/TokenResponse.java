package com.api.estudo.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TokenResponse {

    public TokenResponse(String token) {
        this.token = token;
    }

    private String token;
}
