package com.kraken.spring_app_coworking.Security.interfaces;

import com.kraken.spring_app_coworking.Models.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.util.Date;
import java.util.List;

public interface JWTTokenService {
    String generateAccessToken(Usuario usuario, List<String> roles, List<String> permisos);
    String generateRefreshToken(Usuario usuario);
    // no se valida ni que tenga el bearer, no authorization, solo se verifica el token,si esta todo bien
    // entonces solamente retorna los claims el payload, si no retorna una exception
    Claims getClaimsToken(String token) throws JwtException, ExpiredJwtException;
    Date getExpirationToken(String token) throws JwtException,ExpiredJwtException;
     void invalidarJWT(String token);
     boolean estaTokenEnRedis(String token);
}
