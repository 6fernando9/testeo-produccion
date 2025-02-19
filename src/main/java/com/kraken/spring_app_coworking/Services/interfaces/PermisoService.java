package com.kraken.spring_app_coworking.Services.interfaces;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.Permiso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PermisoService {
    Set<Permiso> findAll();
    Permiso findById(Long id) throws EntityNotFoundException;
    Permiso save(Permiso permiso);
    Permiso updatePermiso(Long id,Permiso permiso) throws EntityNotFoundException;
    void deleteById(Long id) throws EntityNotFoundException;

    //para probar la paginacion
    Page<Permiso> findAllPermisosPaginado(Pageable pageable);
}
