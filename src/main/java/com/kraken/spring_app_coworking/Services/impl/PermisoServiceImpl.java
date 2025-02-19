package com.kraken.spring_app_coworking.Services.impl;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.Permiso;
import com.kraken.spring_app_coworking.Repositories.PermisoRepository;
import com.kraken.spring_app_coworking.Services.interfaces.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PermisoServiceImpl implements PermisoService {
    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public Set<Permiso> findAll() {
        return new HashSet<>(this.permisoRepository.findAll());//usa JPARepository
    }

    @Override
    public Permiso findById(Long id) throws EntityNotFoundException{
        return this.findPermisoById(id);
    }

    @Override
    public Permiso save(Permiso permiso) {
        return this.permisoRepository.save(permiso);
    }

    @Override
    public Permiso updatePermiso(Long id, Permiso permiso) throws EntityNotFoundException{
        Permiso permiso1=this.findPermisoById(id);
        permiso1.setNombre(permiso.getNombre());
        return this.permisoRepository.save(permiso1);
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException
    {
        Optional<Permiso> optionalRol =this.permisoRepository.findById(id);
        if(!optionalRol.isPresent()){
            throw new EntityNotFoundException("Error Permiso no Encontrado");
        }
        this.permisoRepository.deleteById(id);
    }

    @Override
    public Page<Permiso> findAllPermisosPaginado(Pageable pageable) {
        return this.permisoRepository.findAll(pageable);
    }

    //metodo para verificar si existe un Permiso, si existe entonces solamente retorna al objeto permiso
    // si no eixste entonces lanza una excepcion
    private Permiso findPermisoById(Long id) throws EntityNotFoundException {
        Optional<Permiso> optionalRol =this.permisoRepository.findById(id);
        if(!optionalRol.isPresent()){
            throw new EntityNotFoundException("Error Permiso no Encontrado");
        }
        return optionalRol.get();
    }
}
