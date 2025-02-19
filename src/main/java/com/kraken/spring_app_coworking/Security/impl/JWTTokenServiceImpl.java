package com.kraken.spring_app_coworking.Security.impl;

import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Security.interfaces.JWTTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kraken.spring_app_coworking.Security.Auth.TokenJwtConfig.SECRET_KEY;
@Service
public class JWTTokenServiceImpl implements JWTTokenService {

    private static final long DATE_MAXIMA_TOKEN = 3600000L;// equivale a 1hr
    private static final long DATE_MAXIMA_REFRESH_TOKEN = (long) (3600000*2);//equivale a un mes o 720 hr
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     * @param usuario -> recibe el usuario
     * @param roles  -> recibe los roles del usuario
     * @param permisos
     * @return
     */
    @Override
    public String generateAccessToken(Usuario usuario, List<String> roles, List<String> permisos) {
       // System.out.println("generar token: "+usuario);
        Claims claims = Jwts.claims()
                .add("roles", roles)
                .add("permissions", permisos)
                .build();
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + DATE_MAXIMA_TOKEN))
                .signWith(SECRET_KEY)
                .compact();

    }

    /**
     * @param usuario -> recibe un objeto usuario para generar el jwt
     * @return
     */
    @Override
    public String generateRefreshToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + DATE_MAXIMA_REFRESH_TOKEN))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * retorna las claims o mas que todo el payload del token
     *
     * @param token
     * @return
     */
    @Override
    public Claims getClaimsToken(String token) throws JwtException, ExpiredJwtException {
        Claims claims = null;
        try {
            claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            //si la fecha de expiracion es menor que la actual entonces es token invalido
            if (claims.getExpiration().before(new Date()))
                throw new ExpiredJwtException(null, claims, "El token a expirado");
            return claims;
        } catch (JwtException e) {
            throw new JwtException("Error..al validar el token");
        }
    }
    @Override
    public Date getExpirationToken(String token) throws JwtException,ExpiredJwtException{
        Claims claims = this.getClaimsToken(token);
        return claims.getExpiration();
    }


    // Invalidar un token en Redis con el mismo tiempo de expiración que el JWT
    //modificado para que desues de 1 hr (TTl) se elimine el token agregado a redis
    @Override
    public void invalidarJWT(String token) {
        // obtener la fecha de expiracion del jwt
        Date expirationDate = this.getExpirationToken(token);

        // calculamos el tiempo de expiracion en milisegundos
        long expirationTime = expirationDate.getTime() - System.currentTimeMillis();
        long ttl = 1; //1hr, pasando 1hr redis eliminara el token de su cache
        // Guardar el token en Redis con el TTL correspondiente
        redisTemplate.opsForValue().set(token, "invalid", TimeUnit.HOURS.toMillis(ttl), TimeUnit.MILLISECONDS);
    }

    // Verificar si el token está en redis
    @Override
    public boolean estaTokenEnRedis(String token) {
        return redisTemplate.hasKey(token);
    }
}
