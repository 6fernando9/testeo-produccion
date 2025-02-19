package com.kraken.spring_app_coworking.Services.interfaces;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.Permiso;
import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RolService {
    Set<Rol> findAll();
    Rol findById(Long id) throws EntityNotFoundException;
    Rol save(Rol rol);
    Rol updateRol(Long id, Rol rol) throws EntityNotFoundException;
    void deleteById(Long id) throws EntityNotFoundException;
    //metodo para asignar los permisos a un rol

    Set<Permiso> asignarPermisosPorRol(Long id,List<Permiso> permisoList) throws EntityNotFoundException;

    List<UsuarioDTO> getUsuariosByRolName(String rolname);

    List<UsuarioDTO> getUsuariosByRolNames(String rolname);
}
