package com.kraken.spring_app_coworking.Repositories;

import com.kraken.spring_app_coworking.Models.Permiso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository// no se si es necesario por que funcino sin esto igualmente
public interface PermisoRepository extends JpaRepository<Permiso,Long> {

}
