package com.api.estudo.database.queries;

public abstract class AuthenticationQueriesSQL {

    public static final String INSERT_ACCESS_LOG =
            "INSERT INTO LOG_TABLE (userid, datetime) " +
                    "VALUES (:userid, :datetime)";

    public static final String SELECT_DRIVER_NAME_BY_REF =
            "SELECT forename, surname FROM DRIVER " +
            "WHERE DRIVERREF = :ref";

    public static final String SELECT_CONSTRUCTOR_NAME_BY_REF =
            "SELECT name FROM CONSTRUCTORS " +
                    "WHERE CONSTRUCTORREF = :ref";
}
