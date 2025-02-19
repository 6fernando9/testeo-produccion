package com.kraken.spring_app_coworking.Repositories;

import com.kraken.spring_app_coworking.Models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {
}
