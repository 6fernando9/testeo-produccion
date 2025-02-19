package com.kraken.spring_app_coworking.Services.impl;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Mappers.UsuarioMapper;
import com.kraken.spring_app_coworking.Models.Permiso;
import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;
import com.kraken.spring_app_coworking.Repositories.PermisoRepository;
import com.kraken.spring_app_coworking.Repositories.RolRepository;
import com.kraken.spring_app_coworking.Services.interfaces.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public Set<Rol> findAll() {
        return new HashSet<>((Collection) rolRepository.findAll());//usa CRUD Repository
    }

    @Override
    public Rol findById(Long id) throws EntityNotFoundException{
        return this.findRolById(id);
    }

    @Override
    public Rol save(Rol rol) {
        return this.rolRepository.save(rol);
    }

    @Override
    public Rol updateRol(Long id, Rol rol) throws EntityNotFoundException {
        Rol rol1= this.findRolById(id);
        rol1.setNombre(rol.getNombre());
        return this.rolRepository.save(rol1);
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        Optional<Rol> optionalRol =rolRepository.findById(id);
        if(!optionalRol.isPresent()){
            throw new EntityNotFoundException("Error Rol no Encontrado");
        }
        this.rolRepository.deleteById(id);
    }

    /**
     * @param id -> representa el rol del id
     * @param permisoList -> enviamos una lista de roles que agregaremos
     * @return -> retorna una lista de todos los permisos de ese rol
     * @throws EntityNotFoundException -> retorna una exception si se encuentra un permiso que no esta presente en la base de datos
     */
    @Override
    public Set<Permiso> asignarPermisosPorRol(Long id, List<Permiso> permisoList) throws EntityNotFoundException{
        Rol rol=this.findRolById(id);
        Set<Permiso> listaPermisosActual = rol.getPermisos();
        Set<Permiso> permisosValidados=permisoList.stream()
                .map(permiso -> {
                            try {
                                return this.permisoRepository.findById(permiso.getId())
                                        .orElseThrow(()-> new EntityNotFoundException("Error.. permiso no encontrado"));
                            } catch (EntityNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .collect(Collectors.toSet());
        listaPermisosActual.addAll(permisosValidados);
        Rol rolGuardado=this.rolRepository.save(rol);
        return rolGuardado.getPermisos();
    }

    /**
     * problema query n+1 solucionado
     * @param rolname-> recibe el nombre del rol
     * @return -> retorna una lista de usuarios dto
     */
    @Override
    public List<UsuarioDTO> getUsuariosByRolName(String rolname) {
        Rol rol=this.rolRepository.findUsersByRolName(rolname);
        List<Usuario> usuarioList = rol.getUsuarios();
        System.out.println(usuarioList);
        List<UsuarioDTO> usuarioDTOList = UsuarioMapper.INSTANCE.usuarioToUsuarioDTOList(usuarioList);
        return usuarioDTOList;
    }

    //problema query n+1
    /**
     * @param rolname
     * @return
     */
    @Override
    public List<UsuarioDTO> getUsuariosByRolNames(String rolname) {
        Optional<Rol> rol=this.rolRepository.findByNombre(rolname);
        List<Usuario> usuarioList = rol.get().getUsuarios();
        System.out.println(usuarioList);
        List<UsuarioDTO> usuarioDTOList = UsuarioMapper.INSTANCE.usuarioToUsuarioDTOList(usuarioList);
        return usuarioDTOList;
    }


    //metodo para verificar si existe un rol, si existe entonces solamente retorna al objeto rol
    // si no eixste entonces lanza una excepcion
    private Rol findRolById(Long id) throws EntityNotFoundException {
        Optional<Rol> optionalRol =rolRepository.findById(id);
        if(!optionalRol.isPresent()){
            throw new EntityNotFoundException("Error Rol no Encontrado");
        }
        return optionalRol.get();
    }
}
