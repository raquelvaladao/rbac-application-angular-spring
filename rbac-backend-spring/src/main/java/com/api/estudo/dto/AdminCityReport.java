package com.api.estudo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCityReport {

    private String cityName;

    private String iataCode;

    private String airportName;

    private String airportType;

    private String distanceRounded;
}
