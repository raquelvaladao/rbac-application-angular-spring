package com.api.estudo.services;


import com.api.estudo.database.repositories.LogTableRepository;
import com.api.estudo.dto.LoginRequest;
import com.api.estudo.dto.TokenResponse;
import com.api.estudo.exceptions.InvalidInputException;
import com.api.estudo.models.RaceUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class AuthenticationService {

    @Value("${api.jwt.expiration}")
    private String expiration;

    @Value("${api.jwt.secret}")
    private String secret;

    @Value("${api.jwt.issuer}")
    private String issuer;

    private final AuthenticationManager authenticationManager;

    private final LogTableRepository logTableRepository;

    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, LogTableRepository logTableRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.logTableRepository = logTableRepository;
        this.userService = userService;
    }

    public TokenResponse authenticate(LoginRequest request){
        if(Strings.isNullOrEmpty(request.getLogin()) || Strings.isNullOrEmpty(request.getPassword())) {
            throw new InvalidInputException("Login e senha devem estar preenchidos");
        }
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

        String userFullName = userService.getNameByRef(request.getLogin());
        String token = generateToken(authenticate, userFullName);

        return new TokenResponse(token);
    }

    private String generateToken(Authentication authentication, String fullName) {
        RaceUser userToLogIn = (RaceUser) authentication.getPrincipal();
        Date today = new Date();
        Date expireDate = new Date(today.getTime() + Long.parseLong(expiration));

         String token = JWT.create()
                .withClaim("role", userToLogIn.getType().name())
                .withClaim("username", fullName)
                .withSubject(userToLogIn.getUserId().toString())
                .withIssuer(issuer)
                .withExpiresAt(expireDate)
                .sign(this.createAlgorithm());

        log.info("[ AuthenticationService ] - Generating token for user {}", userToLogIn.getLogin());

        logTableRepository.insertLog(userToLogIn.getUserId(), Timestamp.from(Instant.now()));
        log.info("[ AuthenticationService ] - Saving access log.");

        return token;
    }

    private Algorithm createAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public boolean isTokenValid(String token){
        try {
            if (token == null)
                return false;

            JWT.require(this.createAlgorithm()).withIssuer(issuer).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public Integer gatherUserIdFromToken(String token) {
        String subject = JWT.require(this.createAlgorithm()).withIssuer(issuer).build().verify(token).getSubject();

        return Integer.parseInt(subject);

    }


}
