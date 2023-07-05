package com.api.estudo.dto;

import lombok.Data;


@Data
public class NewPilotRequest {

    private String driverRef;
    private Integer number;
    private String code;
    private String forename;
    private String surname;
    private String birthDate;
    private String nationality;
}
