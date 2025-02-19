package com.kraken.spring_app_coworking.Security.Filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.spring_app_coworking.Models.BitacoraUsuario;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.enums.BitacoraUsuarioEnum;
import com.kraken.spring_app_coworking.Repositories.BitacoraUsuarioRepository;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import com.kraken.spring_app_coworking.Security.Auth.dto.LoginResponseDTO;
import com.kraken.spring_app_coworking.Security.impl.JWTTokenServiceImpl;
import com.kraken.spring_app_coworking.Security.interfaces.JWTTokenService;
import com.kraken.spring_app_coworking.Utils.IPUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.kraken.spring_app_coworking.Security.Auth.TokenJwtConfig.*;
//filtro encargado de manejar el login, validar credenciales y generar token JWT , junto a la respuesta HTTP
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    //este repo viene del config ya cargado, esto para poder bucsar al usuario y obtener su id solamente
    private UsuarioRepository usuarioRepository;

    //@Autowired
    private JWTTokenService jwtTokenService;


    private BitacoraUsuarioRepository bitacoraUsuarioRepository;

//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository) {
//        this.authenticationManager = authenticationManager;
//        this.usuarioRepository = usuarioRepository;
//        this.jwtTokenService = new JWTTokenServiceImpl();// lo instancion manualmente
//        this.setFilterProcessesUrl("/auth/login");
//    }
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository,
                                   JWTTokenService jwtTokenService,BitacoraUsuarioRepository bitacoraUsuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtTokenService = jwtTokenService;
        this.bitacoraUsuarioRepository = bitacoraUsuarioRepository;
        this.setFilterProcessesUrl("/auth/login");
    }


    /**
     * @param filterProcessesUrl filtro personalizado de login
     */
    @Override
    public void setFilterProcessesUrl(String filterProcessesUrl) {
        super.setFilterProcessesUrl(filterProcessesUrl);
    }

    //al parecer aqui se crea el token
    //intento de autenticacion
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Usuario user =null;
        String correo =null;
        String password=null;
        //inputStream viene el JSON
        try {
            user = new ObjectMapper().readValue(request.getInputStream(),Usuario.class);
            correo =user.getCorreo();//mediante este valor buscara en el userDetailsService
            password=user.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(correo,password);
        return authenticationManager.authenticate(authenticationToken);

    }
    //cuandoe el login es exitoso, se genera el token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response
            , FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User)authResult.getPrincipal();
        String username= user.getUsername();//obtener el username con el que se autentifica el usuario, que sera su correo en nuestro caso
        Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(user.getUsername());
        if (!optionalUsuario.isPresent()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("Usuario con correo %s no encontrado..",username));
            return;
        }
        Usuario usuario = optionalUsuario.get();
        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        for (GrantedAuthority authority : user.getAuthorities()) {
            String authorityName = authority.getAuthority();
            if (authorityName.startsWith("ROLE_")) {
                roles.add(authorityName);
            } else {
                permissions.add(authorityName);
            }
        }
        String token = this.jwtTokenService.generateAccessToken(usuario,roles,permissions);
        String refreshToken = this.jwtTokenService.generateRefreshToken(usuario);
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN+token);
        response.setContentType(CONTENT_TYPE);
        //agregamos la bitacora
        this.agregarBitacora(request,usuario);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                token,
                refreshToken,
                usuario.getUsername(),
                String.format("Bienvenido %s has iniciado sesion con exito",usuario.getUsername()));
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponseDTO));
        response.setStatus(200);

        //response.sendRedirect("/auth/login/success");///redirijo a otra ruta
    }
    //recibe el objeto User de spring para obtener sus roles y permisos, y el objeto usuario para

    //si el login falla
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        Map<String,String> body = new HashMap<>();
        body.put("message","Error en la autenticacion username o password incorrectos");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);//unauthorized-no autorizado
        response.setContentType(CONTENT_TYPE);

    }

    private void agregarBitacora(HttpServletRequest request,Usuario usuario){
        System.out.println("metodo agregar bitacora");
        BitacoraUsuario bitacoraUsuario = BitacoraUsuario.builder()
                .ip(IPUtils.getClientIP(request.getRemoteAddr()))
                .username(usuario.getUsername())
                .tipoSesion(BitacoraUsuarioEnum.INICIO_DE_SESION.getAbreviacion())
                .usuarioId(usuario.getId())
                .fecha(LocalDateTime.now()).build();
        BitacoraUsuario bitacoraUsuario1 = this.bitacoraUsuarioRepository.save(bitacoraUsuario);
        System.out.println("bitacora guardada con exito " + bitacoraUsuario1);
    }
}
