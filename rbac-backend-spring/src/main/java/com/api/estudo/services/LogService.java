package com.api.estudo.services;


import com.api.estudo.database.repositories.LogTableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@Slf4j
public class LogService {

    private final LogTableRepository logTableRepository;

    public LogService(LogTableRepository logTableRepository) {
        this.logTableRepository = logTableRepository;
    }

    public void logAccess(Integer userId){
        log.info("[ LogService ] - User accessed. Saving log.");
        logTableRepository.insertLog(userId, Timestamp.from(Instant.now()));
    }
}
