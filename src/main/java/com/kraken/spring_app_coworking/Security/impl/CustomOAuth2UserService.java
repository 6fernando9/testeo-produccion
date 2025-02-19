package com.kraken.spring_app_coworking.Security.impl;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.enums.RolEnum;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;

//sevicio que genera y envia el codigo
//esto es como un filtro nada mas
//es decir por aqui podemos obtener el request que nos da google antes de los definidos en el config
//no lo usaremos, pero lo dejaremos como para saber que si existe
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//    @Autowired
//    private UsuarioRepository usuarioRepository;

    /**
     * @param userRequest
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //return super.loadUser(userRequest);
        OAuth2User oauthUser = super.loadUser(userRequest);
        String email = oauthUser.getAttribute("email");
        String username = oauthUser.getAttribute("name");
        System.out.println("load user.."+email);
        System.out.println("username.."+username);


        return oauthUser;
    }
}
