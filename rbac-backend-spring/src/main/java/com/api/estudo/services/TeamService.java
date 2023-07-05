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
public class TeamService {

    private final UserRepository userRepository;

    public TeamService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> getFirstLastPilotDataOverview() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> firstLastYear = userRepository.overviewTeamFirstLastYearData(loggedUser.getOriginalId());

        FirstLastYear response = FirstLastYear.builder()
                .firstYear(firstLastYear.get(0)[0] == null ? "-1" : firstLastYear.get(0)[0].toString())
                .lastYear(firstLastYear.get(0)[0] == null ? "-1" : firstLastYear.get(0)[1].toString())
                .build();

        log.info("Generating pilot first and last year report");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Object> getDistinctPilotsQuantityOverview() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> pilotsQuantity = userRepository.overviewTeamDistinctPilotsQuantity(loggedUser.getOriginalId());

        Integer quantity = pilotsQuantity.get(0)[0] == null ? -1 : Integer.parseInt(pilotsQuantity.get(0)[0].toString());

        log.info("Generating pilot victories quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(PilotVictories.builder().quantity(quantity).build());
    }

    public ResponseEntity<Object> getTeamVictoriesQuantityOverview() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> teamVictoriesQuantity = userRepository.overviewTeamVictoriesQuantity(loggedUser.getOriginalId());

        Integer quantity = teamVictoriesQuantity.get(0)[0] == null ? -1 : Integer.parseInt(teamVictoriesQuantity.get(0)[0].toString());

        log.info("Generating pilot victories quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(PilotVictories.builder().quantity(quantity).build());
    }

    public ResponseEntity<Object> getStatusQuantity() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> statusQuantity = userRepository.reportTeamStatusQuantity(loggedUser.getOriginalId());
        List<Status> list = new ArrayList<>();

        statusQuantity.forEach(tuple -> {
            list.add(
                    Status.builder()
                            .status(tuple[0] == null ? "-" : tuple[0].toString())
                            .quantity(tuple[1] == null ? -1 : Integer.parseInt(tuple[1].toString()))
                            .build()
            );
        });
        log.info("Generating team status quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    public ResponseEntity<Object> getAllAndFirstPilotsReports() {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NameVictories> list = new ArrayList<>();
        List<Object[]> positionQuantity = userRepository.reportTeamPilotsNameAndVictories(loggedUser.getOriginalId());

        positionQuantity.forEach(tuple -> {
            list.add(
                    NameVictories.builder()
                            .name(tuple[0] == null ? "-" : tuple[0].toString())
                            .quantity(tuple[1] == null ? -1 : Integer.parseInt(tuple[1].toString()))
                            .build()
            );
        });
        log.info("[ AdminService ] - Generating team pilots quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    public ResponseEntity<Object> getPilotByForename(String forename) {
        RaceUser loggedUser = (RaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> pilot = userRepository.teamSearch(forename, loggedUser.getOriginalId());
        if(pilot.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }
        PilotSearch response = PilotSearch.builder()
                .name(pilot.get(0)[0] == null ? "-" : pilot.get(0)[0].toString())
                .birth(pilot.get(0)[0] == null ? "-" : pilot.get(0)[1].toString())
                .nationality(pilot.get(0)[0] == null ? "-" : pilot.get(0)[2].toString())
                .build();

        log.info("[ AdminService ] - Generating team pilots quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(List.of(response));
    }
}
