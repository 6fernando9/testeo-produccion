package com.kraken.spring_app_coworking.Repositories;

import com.kraken.spring_app_coworking.Models.BitacoraUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BitacoraUsuarioRepository extends JpaRepository<BitacoraUsuario,Long> {
    /// query methods
    List<BitacoraUsuario> findByUsuarioId(Long id);
    List<BitacoraUsuario> findByUsername(String username);
    //List<BitacoraUsuario> findByTipoSesion(String tipoSesion);
    //pensaba devolver el metodo pero paginado
}
