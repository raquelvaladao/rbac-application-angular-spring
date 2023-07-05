package com.api.estudo.controllers;


import com.api.estudo.services.PilotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pilot")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@Api(tags = "Área de acesso de Pilotos")
public class PilotController {

    private final PilotService pilotService;

    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping("/report/status")
    @ApiOperation(value = "Relatório de quantidade de cada status")
    public ResponseEntity<Object> statusReport(){
        return ResponseEntity.ok(pilotService.getStatusQuantity());
    }

    @GetMapping("/report/victories")
    @ApiOperation(value = "Relatório de quantidade de vitórias")
    public ResponseEntity<Object> victoriesReport(){
        return ResponseEntity.ok(pilotService.getVictoriesRollup());
    }

    @GetMapping("/overview/victories")
    @ApiOperation(value = "Relatório de quantidade de vitórias do piloto")
    public ResponseEntity<Object> reportVictories(){
        return ResponseEntity.ok(pilotService.getVictoriesReport());
    }

    @GetMapping("/overview/races")
    @ApiOperation(value = "Relatório do primeiro e último ano de corrida do piloto")
    public ResponseEntity<Object> reportFirstLastRace(){
        return ResponseEntity.ok(pilotService.getFirstLastRaceOverview());
    }
}
