package com.api.estudo.dto;

import lombok.Data;


@Data
public class NewUserRequest {

    private String login;
    private String password;
    private String type;
}
