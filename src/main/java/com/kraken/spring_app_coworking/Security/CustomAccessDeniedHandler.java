package com.kraken.spring_app_coworking.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.spring_app_coworking.Exceptions.dto.Error;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

//Esta clase se encarga de verificar si el usuario esta autenticado, pero no tiene los permisos necesarios
// lanza una excepcion si el usuario esta autenticado pero no tiene permisos
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
         Error error = Error.builder()
                .date(new Date())
                .status(HttpServletResponse.SC_FORBIDDEN)
                .descripcion("Error usuario no tiene suficientes permisos - Forbidden")
                .mensaje(accessDeniedException.getMessage()).build();
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }

}
