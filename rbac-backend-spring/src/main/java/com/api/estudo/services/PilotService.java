package com.api.estudo.services;


import com.api.estudo.database.repositories.UserRepository;
import com.api.estudo.dto.*;
import com.api.estudo.models.RaceUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PilotService {

    public static final int RACE_FIELD = 2;
    public static final int YEAR_FIELD = 1;
    public static final int QTY_FIELD = 0;
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

    public ResponseEntity<Object> getFirstLastRaceOverview() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> firstLastYear = userRepository.reportGetFirstAndLastYear(loggedUser.getOriginalId());

        FirstLastYear response = FirstLastYear.builder()
                .firstYear(firstLastYear.get(0)[0] == null ? "-1" : firstLastYear.get(0)[0].toString())
                .lastYear(firstLastYear.get(0)[0] == null ? "-1" : firstLastYear.get(0)[1].toString())
                .build();

        log.info("Generating pilot first and last year report");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Object> getStatusQuantity() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> statusQuantity = userRepository.reportGetStatusQuantity(loggedUser.getOriginalId());
        List<Status> list = new ArrayList<>();

        statusQuantity.forEach(tuple -> {
            list.add(
                    Status.builder()
                            .status(tuple[0] == null ? "-" : tuple[0].toString())
                            .quantity(tuple[1] == null ? -1 : Integer.parseInt(tuple[1].toString()))
                            .build()
            );
        });
        log.info("Generating pilot status quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    public ResponseEntity<Object> getVictoriesRollup() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> statusQuantity = userRepository.reportGetVictoriesWithRollup(loggedUser.getOriginalId());
        VictoriesRollup vics = VictoriesRollup.builder().allVictories(new ArrayList<>()).summary(new ArrayList<>()).build();

        statusQuantity.forEach(tuple -> {
            if(tuple[RACE_FIELD] == null && tuple[YEAR_FIELD] ==  null) {
                //it's the total victories
                vics.setTotal(tuple[QTY_FIELD] == null ? "-" : tuple[QTY_FIELD].toString());
            } else if(tuple[RACE_FIELD] == null) {
                // it's a summary
                vics.getSummary().add(
                        VictoriesYear.builder()
                                .year(tuple[YEAR_FIELD]  == null ? "-" : tuple[YEAR_FIELD].toString())
                                .quantity(tuple[QTY_FIELD] == null ? "-" : tuple[QTY_FIELD].toString()).build()
                );
            } else {
                // its a single race quantity
                vics.getAllVictories().add(
                        VictoriesYearRace.builder()
                                .race(tuple[RACE_FIELD] == null ? "-" : tuple[RACE_FIELD].toString())
                                .year(tuple[YEAR_FIELD]  == null ? "-" : tuple[YEAR_FIELD].toString())
                                .quantity(tuple[QTY_FIELD] == null ? "-" : tuple[QTY_FIELD].toString()).build()
                );
            }
        });
        log.info("Generating pilot status quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(vics);
    }
}
