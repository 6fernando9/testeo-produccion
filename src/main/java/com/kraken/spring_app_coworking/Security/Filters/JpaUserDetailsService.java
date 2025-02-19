package com.kraken.spring_app_coworking.Security.Filters;


import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    @Override
    //cada que hagamos login se dispara este metodo, se conecta con la bd
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Optional<Usuario> userOptional = this.usuarioRepository.findByCorreo(correo);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(String.format("el usuario con correo %s no existe en el sistema", correo));
        }

        Usuario usuario = userOptional.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Agregar permisos
        usuario.getRol().getPermisos().forEach(permiso -> {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permiso.getNombre());
            System.out.println("Adding permission: " + authority.getAuthority());
            authorities.add(authority);
        });

        // Agregar rol
        String roleName = "ROLE_" + usuario.getRol().getNombre();
        System.out.println("Adding role: " + roleName);
        authorities.add(new SimpleGrantedAuthority(roleName));

        //se usara el correo para autenticar al usuario y no el username como se utilizo en la anterior implementacion
        return new User(usuario.getCorreo()
                , usuario.getPassword()
                , usuario.isEnabled()
                , true
                , true
                , true
                , authorities);
    }
}
