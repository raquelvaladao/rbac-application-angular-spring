package com.api.estudo.controllers;


import com.api.estudo.dto.NewUserRequest;
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

    @GetMapping
    public ResponseEntity<Object> hello(){
        return ResponseEntity.ok("Hello Admin!");
    }

    @PostMapping
    @ApiOperation(nickname = "Criar usuário", value = "Criar Piloto ou Escuderia", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@RequestBody NewUserRequest userWithTypeRequest) {
        return adminService.createUserWithTypeRequest(userWithTypeRequest);
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

    //TODO
    @GetMapping("/report/test")
    @ApiOperation(value = "Relatório")
    public ResponseEntity<Object> report(){
        return ResponseEntity.ok("Hello Admin! REPORT");
    }
}
