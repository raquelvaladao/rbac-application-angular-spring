package com.api.estudo.controllers;


import com.api.estudo.services.PilotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private PilotService pilotService;

    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping
    public ResponseEntity<Object> hello(){
        return ResponseEntity.ok("Hello Piloto!");
    }

    @GetMapping("/overview")
    @ApiOperation(value = "Overview")
    public ResponseEntity<Object> overview(){
        return ResponseEntity.ok(pilotService.getVictoriesReport());
    }

    @GetMapping("/report/victories")
    @ApiOperation(value = "Relatório de quantidade de vitórias do piloto")
    public ResponseEntity<Object> report(){
        return ResponseEntity.ok("HELLO REPORT");
    }
}
