package com.api.estudo.database.queries;

public abstract class AdminQueriesSQL {

    public static final String ADMIN_INSERT_NEW_USER =
            "INSERT INTO USERS (login, password, type, idOriginal) " +
            "VALUES (:login, MD5(:password), :type, null)";

    public static final String ADMIN_OVERVIEW_TOTAL_PILOTS =
            "SELECT COUNT(*) AS total_pilotos FROM Driver";

    public static final String ADMIN_OVERVIEW_TOTAL_TEAMS =
            "SELECT COUNT(*) AS total_escuderias FROM Constructors";

    public static final String ADMIN_OVERVIEW_TOTAL_RACES =
            "SELECT COUNT(*) AS total_corridas FROM Races";

    public static final String ADMIN_OVERVIEW_TOTAL_SEASONS =
            "SELECT COUNT(DISTINCT year) AS total_temporadas FROM Races";

    public static final String ADMIN_REPORT_POSITION_QUANTITY =
            "SELECT position, COUNT(*) AS quantity " +
            "FROM Results " +
            "GROUP BY position " +
            "ORDER BY position ASC";
}
