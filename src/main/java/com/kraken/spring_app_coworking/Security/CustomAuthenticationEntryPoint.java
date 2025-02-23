package com.kraken.spring_app_coworking.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.spring_app_coworking.Exceptions.dto.Error;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

///clase para customizar los endpoint de authenticacion
/// ///lanza una excepcion si el usuario no esta autenticado
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Error error = Error.builder()
                .date(new Date())
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .descripcion("Error usuario no Autorizado - Unauthorized")
                .mensaje(authException.getMessage()).build();
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
