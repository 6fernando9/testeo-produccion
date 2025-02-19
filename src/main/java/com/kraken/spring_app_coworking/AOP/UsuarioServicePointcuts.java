package com.kraken.spring_app_coworking.AOP;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
//import com.kraken.spring_app_coworking.Security.Filters.JwtAuthenticationFilter;

//@Aspect
@Component
//parece que no funciona desacoplarlo, spring no lo reconoce
public class UsuarioServicePointcuts {

//    @Pointcut("execution(* com.kraken.spring_app_coworking.Security.Filters.JwtAuthenticationFilter.successfulAuthentication(..))")
//    public void usuarioLoginPointcut(){}

    @Pointcut("execution(* com.kraken.spring_app_coworking.Security.Auth.AuthController.logout(..))")
    public void usuarioLogoutPointcut(){}

    @Pointcut("execution(* com.kraken.spring_app_coworking.Security.Auth.AuthController.refreshToken(..))")
    public void usuarioRefreshTokenPointcut(){}
}
