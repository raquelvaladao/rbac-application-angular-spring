package com.api.estudo.controllers;


import com.api.estudo.services.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:4200")
@Api(tags = "Área de acesso de Escuderias")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/overview/victories")
    @ApiOperation(value = "Overview de quantidade de vitórias")
    public ResponseEntity<Object> overviewVictories(){
        return ResponseEntity.ok(teamService.getTeamVictoriesQuantityOverview());
    }

    @GetMapping("/overview/pilots")
    @ApiOperation(value = "Overview de quantidade de pilotos distintos")
    public ResponseEntity<Object> overviewPilots(){
        return ResponseEntity.ok(teamService.getDistinctPilotsQuantityOverview());
    }

    @GetMapping("/overview/years-data")
    @ApiOperation(value = "Overview de ano início e fim contendo dados dos pilotos baseando-se nos resultados de corrida")
    public ResponseEntity<Object> overviewYearsData(){
        return ResponseEntity.ok(teamService.getFirstLastPilotDataOverview());
    }

    @GetMapping("/report/status")
    @ApiOperation(value = "Status e suas quantidades para escuderia")
    public ResponseEntity<Object> reportStatus(){
        return ResponseEntity.ok(teamService.getStatusQuantity());
    }

    @GetMapping("/report/pilots")
    @ApiOperation(value = "Todos os pilotos e suas quantidades de primeiro lugar para escuderia")
    public ResponseEntity<Object> reportFirstPilotsAndAll(){
        return ResponseEntity.ok(teamService.getAllAndFirstPilotsReports());
    }
    @GetMapping("/search/pilot")
    @ApiOperation(value = "Busca de piloto da escuderia")
    public ResponseEntity<Object> searchTeamPilot(@RequestParam("forename") String forename){
        return ResponseEntity.ok(teamService.getPilotByForename(forename));
    }

}
