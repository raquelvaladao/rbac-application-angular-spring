package com.api.estudo.dto;


import lombok.Data;

@Data
public class NewTeamRequest {

    private String constructorRef;
    private String name;
    private String nationality;
    private String url;
}
