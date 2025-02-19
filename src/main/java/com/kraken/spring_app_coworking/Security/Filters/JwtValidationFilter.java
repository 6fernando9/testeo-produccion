package com.kraken.spring_app_coworking.Security.Filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import com.kraken.spring_app_coworking.Security.impl.JWTTokenServiceImpl;
import com.kraken.spring_app_coworking.Security.interfaces.JWTTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static com.kraken.spring_app_coworking.Security.Auth.TokenJwtConfig.*;
//filtro que se asegura que cada solicitud tenga un token valido antes de permitir acceso
//extrae y valida el token
public class JwtValidationFilter extends BasicAuthenticationFilter {
    private JWTTokenService jwtTokenService;

    public JwtValidationFilter(AuthenticationManager authenticationManager,JWTTokenService jwtTokenService) {
        super(authenticationManager);
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            System.out.println(header);
            if(header== null)
                System.out.println("no tengo header");
            else if(!header.startsWith(PREFIX_TOKEN))
                System.out.println("bearer no empieza con el prefix");
            chain.doFilter(request,response);
            return;
        }
        String token = header.replace(PREFIX_TOKEN, "");

        if(this.jwtTokenService.estaTokenEnRedis(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido o expirado");
            return;
        }

        System.out.println("el header no es nulo y si empieza con bearer token");
        try {
            // toca decodificar el token
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            // Obtener roles y permisos
            List<String> roles = claims.get("roles", List.class);
            List<String> permissions = claims.get("permissions", List.class);
            System.out.println("URL requested: " + request.getRequestURI());
            System.out.println("Method: " + request.getMethod());
            System.out.println("Roles from token: " + roles);
            System.out.println("Permissions from token: " + permissions);
            // Obtener ID de usuario (sub)
            String userId = claims.getSubject();
            System.out.println("validation id usuario: "+userId);
            System.out.println("VALIDATION FILTER");
            // Convertir roles y permisos a GrantedAuthority
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (roles != null) {
                roles.forEach(role -> {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    System.out.println("Adding role authority: " + authority.getAuthority());
                    authorities.add(authority);
                });
            }
            if (permissions != null) {
                permissions.forEach(permission -> {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
                    System.out.println("Adding permission authority: " + authority.getAuthority());
                    authorities.add(authority);
                });
            }

            // Configurar el contexto de seguridad
            //System.out.println("validation: "+authorities);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("usuario guardado en el contexto");
            System.out.println("get authentication " +  SecurityContextHolder.getContext().getAuthentication());
             System.out.println("usuario authenticado: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            System.out.println("DESPUES Authorities en el contexto: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        } catch (JwtException e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token JWT es invalido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
            return;
         //   chain.doFilter(request,response);
        }
        chain.doFilter(request,response);
    }
}
