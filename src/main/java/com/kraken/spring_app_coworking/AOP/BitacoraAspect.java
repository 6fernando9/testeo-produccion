package com.kraken.spring_app_coworking.AOP;

import com.kraken.spring_app_coworking.Models.BitacoraUsuario;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.enums.BitacoraUsuarioEnum;
import com.kraken.spring_app_coworking.Repositories.BitacoraUsuarioRepository;
import com.kraken.spring_app_coworking.Security.impl.AuthService;
import com.kraken.spring_app_coworking.Utils.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class BitacoraAspect {

    @Autowired
    private AuthService authService;// FALLLARA SOLAMENTE SI ALGUNOS METODOS DEL SPRING SECURITY QUEDAN DEPRECADOS, LO CUAL NO GENERARA BIEN EL JWT

    @Autowired
    private BitacoraUsuarioRepository bitacoraUsuarioRepository;

    //hace  referencia al login con oauth2
    @AfterReturning(pointcut = "execution(* com.kraken.spring_app_coworking.Security.Auth.OAuth2Controller.oauth2Success(..))")
    //@AfterReturning(pointcut = "usuarioLoginPointcut()")
    public void afterSuccesfullAuthentication(JoinPoint joinPoint){
        System.out.println("ingrese al succesful");
        Usuario usuarioAutenticado = this.authService.getAuthenticatedUser();
        //OAuth2User oAuth2User = (OAuth2User) joinPoint.getArgs()[0];
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[1];//accedo a los parametros
        BitacoraUsuario bitacoraUsuario = BitacoraUsuario.builder()
                .ip(IPUtils.getClientIP(request.getRemoteAddr()))
                .username(usuarioAutenticado.getUsername())
                .tipoSesion(BitacoraUsuarioEnum.INICIO_DE_SESION.getAbreviacion())
                .usuarioId(usuarioAutenticado.getId())
                .fecha(LocalDateTime.now()).build();
        BitacoraUsuario bitacoraUsuario1 = this.bitacoraUsuarioRepository.save(bitacoraUsuario);
        System.out.println("bitacora guardada con exito " + bitacoraUsuario1);
    }
    

    @AfterReturning(pointcut = "execution(* com.kraken.spring_app_coworking.Security.Auth.AuthController.logout(..))",returning = "response")
    //@AfterReturning(pointcut = "usuarioLogoutPointcut()", returning = "response")
    public void afterSuccessfullLogout(JoinPoint joinPoint,ResponseEntity<String> response){
        Usuario usuarioAutenticado = this.authService.getAuthenticatedUser();
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[1];
        BitacoraUsuario bitacoraUsuario = BitacoraUsuario.builder()
                .ip(IPUtils.getClientIP(request.getRemoteAddr()))
                .username(usuarioAutenticado.getUsername())
                .tipoSesion(BitacoraUsuarioEnum.CIERRE_DE_SESION.getAbreviacion())
                .usuarioId(usuarioAutenticado.getId())
                .fecha(LocalDateTime.now()).build();
        BitacoraUsuario bitacoraUsuario1 = this.bitacoraUsuarioRepository.save(bitacoraUsuario);
        System.out.println("bitacora guardada con exito " + bitacoraUsuario1);
    }

    @AfterReturning(pointcut = "execution(* com.kraken.spring_app_coworking.Security.Auth.AuthController.refreshToken(..))")
    //@AfterReturning(pointcut = "usuarioRefreshTokenPointcut()")
    public void afterSuccessfullRefreshToken(JoinPoint joinPoint){
        Usuario authenticatedUser = this.authService.getAuthenticatedUser();
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[1];
        BitacoraUsuario bitacoraUsuario = BitacoraUsuario.builder()
                .ip(IPUtils.getClientIP(request.getRemoteAddr()))
                .username(authenticatedUser.getUsername())
                .tipoSesion(BitacoraUsuarioEnum.REFRESH_TOKEN.getAbreviacion())
                .usuarioId(authenticatedUser.getId())
                .fecha(LocalDateTime.now()).build();
        BitacoraUsuario bitacoraUsuario1 = this.bitacoraUsuarioRepository.save(bitacoraUsuario);
        System.out.println("bitacora guardada con exito " + bitacoraUsuario1);
    }
}
