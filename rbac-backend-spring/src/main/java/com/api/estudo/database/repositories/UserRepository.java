package com.api.estudo.database.repositories;


import com.api.estudo.database.queries.AdminQueriesSQL;
import com.api.estudo.database.queries.PilotQueriesSQL;
import com.api.estudo.models.RaceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<RaceUser, Integer> {

    Optional<RaceUser> findByLogin(String login);

    @Modifying
    @Transactional
    @Query(value = AdminQueriesSQL.ADMIN_INSERT_NEW_USER, nativeQuery = true)
    void insertUser(@Param("login") String login,
                    @Param("password") String password,
                    @Param("type") String type
    );

    @Query(value = AdminQueriesSQL.ADMIN_OVERVIEW_TOTAL_PILOTS, nativeQuery = true)
    List<Object[]> overviewTotalPilots();

    @Query(value = AdminQueriesSQL.ADMIN_OVERVIEW_TOTAL_TEAMS, nativeQuery = true)
    List<Object[]> overviewTotalTeams();

    @Query(value = AdminQueriesSQL.ADMIN_OVERVIEW_TOTAL_RACES, nativeQuery = true)
    List<Object[]> overviewTotalRaces();

    @Query(value = AdminQueriesSQL.ADMIN_OVERVIEW_TOTAL_SEASONS, nativeQuery = true)
    List<Object[]> overviewTotalSeasons();

    @Query(value = AdminQueriesSQL.ADMIN_REPORT_POSITION_QUANTITY, nativeQuery = true)
    List<Object[]> reportGetEachPositionQuantity();

    @Query(value = PilotQueriesSQL.PILOT_REPORT_VICTORIES, nativeQuery = true)
    List<Object[]> reportGetVictoriesQuantity(@Param("DRIVERID") Integer driverId);
}
