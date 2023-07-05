package com.api.estudo.database.repositories;


import com.api.estudo.database.queries.AdminQueriesSQL;
import com.api.estudo.database.queries.PilotQueriesSQL;
import com.api.estudo.database.queries.TeamQueriesSQL;
import com.api.estudo.models.RaceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<RaceUser, Integer> {

    Optional<RaceUser> findByLogin(String login);

    @Modifying
    @Transactional
    @Query(value = AdminQueriesSQL.ADMIN_INSERT_NEW_PILOT, nativeQuery = true)
    void insertPilot(
            @Param("driverref") String driverRef,
            @Param("number") Integer number,
            @Param("code") String code,
            @Param("forename") String forename,
            @Param("surname") String surname,
            @Param("dob") Date dob,
            @Param("nationality") String nationality
    );

    @Modifying
    @Transactional
    @Query(value = AdminQueriesSQL.ADMIN_INSERT_NEW_TEAM, nativeQuery = true)
    void createNewTeam(
            @Param("constructorRef") String constructorRef,
            @Param("name") String name,
            @Param("nationality") String nationality,
            @Param("url") String url
    );

    @Query(value = AdminQueriesSQL.ADMIN_REPORT_CITIES, nativeQuery = true)
    List<Object[]> reportAirports(@Param("CITY_NAME") String cityName);

    @Query(value = AdminQueriesSQL.ADMIN_OVERVIEW_TOTAL_PILOTS, nativeQuery = true)
    List<Object[]> overviewTotalPilots();

    @Query(value = AdminQueriesSQL.ADMIN_OVERVIEW_TOTAL_TEAMS, nativeQuery = true)
    List<Object[]> overviewTotalTeams();

    @Query(value = AdminQueriesSQL.ADMIN_OVERVIEW_TOTAL_RACES, nativeQuery = true)
    List<Object[]> overviewTotalRaces();

    @Query(value = AdminQueriesSQL.ADMIN_OVERVIEW_TOTAL_SEASONS, nativeQuery = true)
    List<Object[]> overviewTotalSeasons();

    @Query(value = AdminQueriesSQL.ADMIN_REPORT_STATUS_QUANTITY, nativeQuery = true)
    List<Object[]> reportGetEachPositionQuantity();

    // PILOT OVERVIEW

    @Query(value = PilotQueriesSQL.PILOT_OVERVIEW_VICTORIES, nativeQuery = true)
    List<Object[]> reportGetVictoriesQuantity(@Param("DRIVER_ID") Integer driverId);

    @Query(value = PilotQueriesSQL.PILOT_OVERVIEW_FIRST_LAST_YEAR, nativeQuery = true)
    List<Object[]> reportGetFirstAndLastYear(@Param("ID") Integer driverId);

    // PILOT REPORTS

    @Query(value = PilotQueriesSQL.PILOT_REPORT_STATUS, nativeQuery = true)
    List<Object[]> reportGetStatusQuantity(@Param("DRIVER_ID") Integer originalId);

    @Query(value = PilotQueriesSQL.PILOT_REPORT_ROLLUP, nativeQuery = true)
    List<Object[]> reportGetVictoriesWithRollup(@Param("DRIVER_ID") Integer originalId);

    // TEAM OVERVIEW
    @Query(value = TeamQueriesSQL.TEAM_OVERVIEW_VICTORIES_QUANTITY, nativeQuery = true)
    List<Object[]> overviewTeamVictoriesQuantity(@Param("ID") Integer originalId);

    @Query(value = TeamQueriesSQL.TEAM_OVERVIEW_PILOTS_QUANTITY, nativeQuery = true)
    List<Object[]> overviewTeamDistinctPilotsQuantity(@Param("ID") Integer originalId);

    @Query(value = TeamQueriesSQL.TEAM_OVERVIEW_FIRST_LAST_YEAR_DATA, nativeQuery = true)
    List<Object[]> overviewTeamFirstLastYearData(@Param("ID") Integer originalId);


    // TEAM REPORTS
    @Query(value = TeamQueriesSQL.TEAM_ALL_AND_FIRST_PILOTS_REPORT, nativeQuery = true)
    List<Object[]> reportTeamPilotsNameAndVictories(@Param("TEAM_ID") Integer originalId);

    @Query(value = TeamQueriesSQL.TEAM_STATUS_REPORT, nativeQuery = true)
    List<Object[]> reportTeamStatusQuantity(@Param("TEAM_ID") Integer originalId);

    // TEAM SEARCH
    @Query(value = TeamQueriesSQL.TEAM_SEARCH, nativeQuery = true)
    List<Object[]> teamSearch(@Param("FORENAME") String forename, @Param("TEAM") Integer originalId);



}
