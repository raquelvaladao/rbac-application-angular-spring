package com.api.estudo.database.queries;

public abstract class TeamQueriesSQL {

    public static final String TEAM_OVERVIEW_VICTORIES_QUANTITY =
            "SELECT team_victories_quantity(:ID)";

    public static final String TEAM_OVERVIEW_PILOTS_QUANTITY =
            "SELECT disctinct_pilots_quantity(:ID)";

    public static final String TEAM_OVERVIEW_FIRST_LAST_YEAR_DATA =
            "SELECT team_first_last_year_data(:ID)";

    public static final String TEAM_ALL_AND_FIRST_PILOTS_REPORT =
            "SELECT " +
                    "CONCAT(D.Forename, ' ', D.Surname) AS pilot_name, " +
                    "COUNT(CASE WHEN R.Position = 1 THEN 1 ELSE NULL END) AS victories " +
                    "FROM Driver D " +
                    "JOIN Results R ON D.DriverId = R.DriverId " +
                    "WHERE R.ConstructorId = :TEAM_ID " +
                    "GROUP BY D.DriverId, D.Forename, D.Surname " +
                    "ORDER BY victories DESC";

    public static final String TEAM_STATUS_REPORT =
            "SELECT " +
                    "S.Status as status,  " +
                    "COUNT(*) AS quantity " +
                    "FROM Results R " +
                    "JOIN Status S ON R.StatusId = S.StatusId " +
                    "WHERE R.ConstructorId = :TEAM_ID " +
                    "GROUP BY S.Status " +
                    "ORDER BY quantity DESC";
    public static final String TEAM_SEARCH =
            "SELECT CONCAT(D.Forename, ' ', D.Surname) AS full_name, D.DOB AS date_of_birth, D.Nationality " +
                    "FROM Driver D " +
                    "JOIN Results R ON D.DriverId = R.DriverId " +
                    "JOIN Constructors C ON R.ConstructorId = C.ConstructorId " +
                    "WHERE D.Forename = :FORENAME " +
                    "  AND C.ConstructorId = :TEAM";
}
