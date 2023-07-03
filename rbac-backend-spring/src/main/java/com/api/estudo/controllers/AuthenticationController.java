package com.api.estudo.controllers;


import com.api.estudo.dto.LoginRequest;
import com.api.estudo.dto.TokenResponse;
import com.api.estudo.services.AuthenticationService;
import com.api.estudo.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Slf4j
@Api(tags = "Área de acesso para autenticação")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(nickname = "Autenticação", response = TokenResponse.class, value = "Login de usuário da tabela Users")
    @PostMapping
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }
}