package com.api.estudo.database.repositories;

import com.api.estudo.database.queries.AuthenticationQueriesSQL;
import com.api.estudo.models.Constructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstructorRepository extends JpaRepository<Constructor, Integer> {

    @Query(value = AuthenticationQueriesSQL.SELECT_CONSTRUCTOR_NAME_BY_REF, nativeQuery = true)
    List<Object[]> selectConstructorGivenRef(@Param("ref") String personRef);
}
