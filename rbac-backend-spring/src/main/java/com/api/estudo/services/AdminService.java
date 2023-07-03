package com.api.estudo.services;

import com.api.estudo.dto.AdminPositionQuantityReport;
import com.api.estudo.dto.AdminReport;
import com.api.estudo.dto.ApiResponse;
import com.api.estudo.dto.NewUserRequest;
import com.api.estudo.database.repositories.UserRepository;
import com.api.estudo.enums.UserType;
import com.api.estudo.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> createUserWithTypeRequest(NewUserRequest newUserRequest) {
        if(!UserType.isValid(newUserRequest.getType()))
            throw new InvalidInputException("Tipo do usuário não aceito");

        if(Strings.isBlank(newUserRequest.getLogin()) || Strings.isBlank(newUserRequest.getPassword()))
            throw new InvalidInputException("Login e senha do usuário obrigatórias");

        userRepository.insertUser(newUserRequest.getLogin(), newUserRequest.getPassword(), newUserRequest.getType());

        log.info("[ AdminService ] - Created new user.");

        ApiResponse response = ApiResponse
                .builder()
                .code("SUCESSO")
                .message(newUserRequest.getLogin() + " criado!")
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
                            .position(tuple[0] == null ? "null" : tuple[0].toString())
                            .quantity(tuple[1] == null ? -1 : Integer.parseInt(tuple[1].toString()))
                            .build()
            );
        });
        log.info("[ AdminService ] - Generating admin position quantity report");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
