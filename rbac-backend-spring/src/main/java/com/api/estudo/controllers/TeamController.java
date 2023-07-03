package com.api.estudo.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:4200")
@Api(tags = "Área de acesso de Escuderias")
public class TeamController {

    @GetMapping
    public ResponseEntity<Object> hello(){
        return ResponseEntity.ok("Hello Escuderia!");
    }

    @GetMapping("/overview")
    @ApiOperation(value = "Overview")
    public ResponseEntity<Object> overview(){
        return ResponseEntity.ok("Hello Escuderia! OVERVIEW");
    }

    @GetMapping("/report")
    @ApiOperation(value = "Relatório")
    public ResponseEntity<Object> report() {
        return ResponseEntity.ok("Hello Escuderia! REPORT");
    }
}
