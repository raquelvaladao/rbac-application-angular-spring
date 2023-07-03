package com.api.estudo.database.repositories;


import com.api.estudo.database.queries.AuthenticationQueriesSQL;
import com.api.estudo.dto.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Repository
public interface LogTableRepository extends JpaRepository<AccessLog, Long> {

    @Modifying
    @Transactional
    @Query(value = AuthenticationQueriesSQL.INSERT_ACCESS_LOG, nativeQuery = true)
    void insertLog(@Param("userid") Integer login,
                    @Param("datetime") Timestamp timestamp);
}
