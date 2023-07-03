package com.api.estudo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminReport {

    private String totalPilots;
    private String totalTeams;
    private String totalRaces;
    private String totalSeasons;
}
