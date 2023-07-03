package com.api.estudo.services;


import com.api.estudo.database.repositories.UserRepository;
import com.api.estudo.dto.PilotVictories;
import com.api.estudo.models.RaceUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PilotService {

    private UserRepository userRepository;

    public PilotService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> getVictoriesReport() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> victories = userRepository.reportGetVictoriesQuantity(loggedUser.getOriginalId());

        Integer quantity = victories.get(0)[0] == null ? -1 : Integer.parseInt(victories.get(0)[0].toString());

        log.info("Generating pilot victories quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(PilotVictories.builder().quantity(quantity).build());
    }
}
