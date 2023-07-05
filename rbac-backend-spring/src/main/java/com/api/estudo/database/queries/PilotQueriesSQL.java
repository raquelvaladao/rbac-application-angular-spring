package com.api.estudo.database.queries;

public abstract class PilotQueriesSQL {

    public static final String PILOT_OVERVIEW_VICTORIES =
            "SELECT get_pilot_victories(:DRIVER_ID)";

    public static final String PILOT_OVERVIEW_FIRST_LAST_YEAR =
            "SELECT get_first_and_last_race_years_from_pilot(:ID)";

    public static final String PILOT_REPORT_STATUS =
            "SELECT S.status, COUNT(*) AS quantidade_resultados " +
                    "FROM RESULTS " +
                    "JOIN STATUS S ON S.STATUSID = RESULTS.STATUSID " +
                    "WHERE driverId = :DRIVER_ID " +
                    "GROUP BY S.statusId " +
                    "ORDER BY quantidade_resultados DESC";

    public static final String PILOT_REPORT_ROLLUP =
            "SELECT " +
                    "COUNT(*) AS quantidade, " +
                    "YEAR as ano, " +
                    "R.Name as corrida " +
                    "FROM Results RS " +
                    "JOIN Races R ON RS.RaceId = R.RaceId " +
                    "JOIN driver d ON RS.driverid = D.driverid " +
                    "WHERE " +
                    "    RS.DriverId = :DRIVER_ID " +
                    "    AND RS.PositionText = '1' " +
                    "GROUP BY ROLLUP (R.Year, R.Name)  " +
                    "ORDER BY R.Year DESC";
}
