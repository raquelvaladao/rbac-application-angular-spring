package com.api.estudo.controllers;


import com.api.estudo.dto.NewPilotRequest;
import com.api.estudo.dto.NewTeamRequest;
import com.api.estudo.services.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@Api(tags = "Área de acesso de Admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/pilot")
    @ApiOperation(nickname = "Criar piloto", value = "Criar Piloto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPilot(@RequestBody NewPilotRequest newPilotRequest) {
        return adminService.createNewPilot(newPilotRequest);
    }

    @PostMapping("/team")
    @ApiOperation(nickname = "Criar escuderia", value = "Criar escuderia", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createTeam(@RequestBody NewTeamRequest newTeamRequest) {
        return adminService.createNewTeam(newTeamRequest);
    }

    @GetMapping("/overview")
    @ApiOperation(value = "Overview")
    public ResponseEntity<Object> overview(){
        return ResponseEntity.ok(adminService.getOverview());
    }

    @GetMapping("/report/position")
    @ApiOperation(value = "Relatório de contagem de pessoas por cada posição na corrida")
    public ResponseEntity<Object> reportPositionQuantity(){
        return ResponseEntity.ok(adminService.getPositionReport());
    }

    @GetMapping("/report/cities")
    @ApiOperation(value = "Relatório de aeroportos próximos a uma cidade num raio de 100km")
    public ResponseEntity<Object> reportCity(@RequestParam("cityName") String cityName){
        return ResponseEntity.ok(adminService.getCityAirportReport(cityName));
    }
}
