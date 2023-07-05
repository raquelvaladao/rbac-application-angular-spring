package com.api.estudo.services;

import com.api.estudo.dto.*;
import com.api.estudo.database.repositories.UserRepository;
import com.api.estudo.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> createNewPilot(NewPilotRequest newPilotRequest) {
        if(Strings.isBlank(newPilotRequest.getDriverRef()))
            throw new InvalidInputException("Driverref do usuário obrigatória");

        Date sqlDate = formatBirthDate(newPilotRequest);

        userRepository.insertPilot(
                newPilotRequest.getDriverRef(),
                newPilotRequest.getNumber(),
                newPilotRequest.getCode(),
                newPilotRequest.getForename(),
                newPilotRequest.getSurname(),
                sqlDate,
                newPilotRequest.getNationality()
        );
        log.info("[ AdminService ] - Created new pilot.");
        ApiResponse response = ApiResponse
                .builder()
                .code("SUCESSO")
                .message(newPilotRequest.getForename() + " criado!")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<Object> createNewTeam(NewTeamRequest newTeamRequest) {
        if(Strings.isBlank(newTeamRequest.getConstructorRef()) || Strings.isBlank(newTeamRequest.getName()))
            throw new InvalidInputException("ConstructorRef do usuário obrigatória");

        userRepository.createNewTeam(
                newTeamRequest.getConstructorRef(),
                newTeamRequest.getName(),
                newTeamRequest.getNationality(),
                newTeamRequest.getUrl()
        );
        log.info("[ AdminService ] - Created new team.");
        ApiResponse response = ApiResponse
                .builder()
                .code("SUCESSO")
                .message(newTeamRequest.getName() + " criado!")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<Object> getOverview() {
        List<Object[]> totalPilots = userRepository.overviewTotalPilots();
        List<Object[]> totalTeams = userRepository.overviewTotalTeams();
        List<Object[]> totalRaces = userRepository.overviewTotalRaces();
        List<Object[]> totalSeasons = userRepository.overviewTotalSeasons();

        AdminReport report = AdminReport.builder()
                .totalPilots(totalPilots.get(0)[0].toString())
                .totalRaces(totalRaces.get(0)[0].toString())
                .totalTeams(totalTeams.get(0)[0].toString())
                .totalSeasons(totalSeasons.get(0)[0].toString())
                .build();

        log.info("[ AdminService ] - Generating admin overview");

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    public ResponseEntity<Object> getPositionReport() {
        List<AdminPositionQuantityReport> list = new ArrayList<>();
        List<Object[]> positionQuantity = userRepository.reportGetEachPositionQuantity();

        positionQuantity.forEach(tuple -> {
            list.add(
                    AdminPositionQuantityReport.builder()
                            .status(tuple[0] == null ? "-" : tuple[0].toString())
                            .quantity(tuple[1] == null ? -1 : Integer.parseInt(tuple[1].toString()))
                            .build()
            );
        });
        log.info("[ AdminService ] - Generating admin position quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    public ResponseEntity<Object> getCityAirportReport(String cityName) {
        List<AdminCityReport> list = new ArrayList<>();
        List<Object[]> airports = userRepository.reportAirports(cityName);

        airports.forEach(tuple -> {
            list.add(
                    AdminCityReport.builder()
                            .cityName(tuple[0] == null ? "-" : tuple[0].toString())
                            .iataCode(tuple[1] == null ? "-" : tuple[1].toString())
                            .airportName(tuple[2] == null ? "-" : tuple[2].toString())
                            .airportType(tuple[3] == null ? "-" : tuple[3].toString())
                            .distanceRounded(tuple[4] == null ? "0" : tuple[4].toString())
                            .build()
            );
        });
        log.info("[ AdminService ] - Generating admin city report");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    private Date formatBirthDate(NewPilotRequest newPilotRequest)  {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = formatter.parse(newPilotRequest.getBirthDate());
            return new Date(date.getTime());
        } catch (Exception e) {
            return null;
        }
    }
}
