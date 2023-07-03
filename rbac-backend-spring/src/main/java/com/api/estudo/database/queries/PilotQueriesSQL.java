package com.api.estudo.database.queries;

public abstract class PilotQueriesSQL {


    public static final String PILOT_REPORT_VICTORIES =
            "SELECT COUNT(*) AS quantity " +
                    "FROM RESULTS " +
                    "WHERE driverId = :DRIVERID " +
                    "AND rank = 1;";
}
