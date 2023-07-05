package com.api.estudo.database.queries;

public abstract class AdminQueriesSQL {

    public static final String ADMIN_INSERT_NEW_PILOT =
            "INSERT INTO DRIVER" +
                    "(driverref, number, code, forename, surname, dob, nationality)" +
                    "VALUES(:driverref, :number, :code, :forename, :surname, :dob, :nationality)";

    public static final String ADMIN_INSERT_NEW_TEAM =
            "INSERT INTO CONSTRUCTORS" +
                    "(constructorref, name, nationality, url)" +
                    "VALUES(:constructorRef, :name, :nationality, :url)";


    public static final String ADMIN_OVERVIEW_TOTAL_PILOTS =
            "SELECT COUNT(*) AS total_pilotos FROM Driver";

    public static final String ADMIN_OVERVIEW_TOTAL_TEAMS =
            "SELECT COUNT(*) AS total_escuderias FROM Constructors";

    public static final String ADMIN_OVERVIEW_TOTAL_RACES =
            "SELECT COUNT(*) AS total_corridas FROM Races";

    public static final String ADMIN_OVERVIEW_TOTAL_SEASONS =
            "SELECT COUNT(DISTINCT year) AS total_temporadas FROM Races";

    public static final String ADMIN_REPORT_STATUS_QUANTITY =
            "SELECT S.Status as status, COUNT(*) AS quantity " +
                    "FROM Status S " +
                    "JOIN Results R ON R.StatusId = S.StatusId " +
                    "GROUP BY S.Status " +
                    "ORDER BY quantity DESC";

    public static final String ADMIN_REPORT_CITIES =
            "SELECT C.Name AS city_name, " +
                    "       A.IATACode AS iata_code, " +
                    "       A.Name AS airport_name, " +
                    "       A.Type AS airport_type, " +
                    "       ROUND( " +
                    "   CAST((earth_distance(ll_to_earth(C.Lat, C.Long), ll_to_earth(A.LatDeg, A.LongDeg)) / 1000) as numeric), 2 " +
                    "   ) AS distance_km_rounded " +
                    "FROM Airports A " +
                    "JOIN GeoCities15K C ON " +
                    "ROUND( " +
                    "   CAST((earth_distance(ll_to_earth(C.Lat, C.Long), ll_to_earth(A.LatDeg, A.LongDeg)) / 1000) as numeric), 2 " +
                    "   ) <= 100 " +
                    "WHERE C.Name = :CITY_NAME AND " +
                    "      A.Type IN ('medium_airport', 'large_airport') " +
                    "ORDER BY distance_km_rounded";
}
