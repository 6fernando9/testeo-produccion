package com.kraken.spring_app_coworking.Security.impl;

import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
//clase para obtener el id y los permisos del usuario cuando sea, siempre y cuando se inyecte esta clase
@Service
public class AuthService {
    //viene aqui luego del validation filter
    //donde el username es el id del usuario
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("soy el principal "+authentication.getPrincipal());
        if(authentication != null){
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                String email = oauth2User.getAttribute("email");
                return this.usuarioRepository.findByCorreo(email).orElseThrow(() -> new RuntimeException("Error Usuario OAuth con correo no identificado.."));
            }
            //por defecto cada usuario registrado lo tenemos con el id de tipo String
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                String userId = (String)authentication.getPrincipal();
                return this.usuarioRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("Error Usuario normalito con id no identificado.."));
            }
        }

        throw new RuntimeException("No hay un usuario autenticado");
    }

    public List<String> getAuthenticatedUserRolesAndPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities()
                    .stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("No hay authentication al obtener roles");
    }

    public Authentication getAuthenticationUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            return authentication;
        }
        throw new RuntimeException("No existe un usuario autenticado..");
    }
}
