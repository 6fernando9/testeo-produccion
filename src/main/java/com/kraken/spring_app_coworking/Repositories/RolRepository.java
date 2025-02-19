package com.kraken.spring_app_coworking.Repositories;

import com.kraken.spring_app_coworking.Models.Rol;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends CrudRepository<Rol,Long> {
    @Query("SELECT r FROM Rol r LEFT JOIN FETCH r.usuarios WHERE r.nombre = :rolname")
    Rol findUsersByRolName(@Param("rolname") String rolname);
    //usando query methods
    Optional<Rol> findByNombre(String nombre);
}
