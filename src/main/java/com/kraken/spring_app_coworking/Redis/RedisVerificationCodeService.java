package com.kraken.spring_app_coworking.Redis;

import com.kraken.spring_app_coworking.Models.dto.UsuarioRedisDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisVerificationCodeService {
    @Autowired
    private RedisTemplate<String, UsuarioRedisDTO> usuarioRedisDTORedisTemplate;
    public static String KEY_STRING = "verification: ";

    public void guardarCodigo(UsuarioRedisDTO usuarioRedisDTO){
        limpiarClavesDeCorreoAsociado(usuarioRedisDTO.getCorreo());
        usuarioRedisDTORedisTemplate.opsForValue().set(KEY_STRING + usuarioRedisDTO.getCodigo(),usuarioRedisDTO,10, TimeUnit.MINUTES);
    }

    public UsuarioRedisDTO getData(String codigo){
        return usuarioRedisDTORedisTemplate.opsForValue().get(codigo);
    }
    public boolean isPresent(String key){
        return this.usuarioRedisDTORedisTemplate.hasKey(key);
    }
    public void limpiarClavesDeCorreoAsociado(String email){
        //obtiene todas las claves que comienzan con verification
        Set<String> keys = usuarioRedisDTORedisTemplate.keys(KEY_STRING + "*");
        if (keys != null) {
            for (String key : keys) {
                UsuarioRedisDTO usuarioRedisDTO = usuarioRedisDTORedisTemplate.opsForValue().get(key);
                if (usuarioRedisDTO.getCorreo().equals(email)) {
                    usuarioRedisDTORedisTemplate.delete(key);
                }
            }
        }
    }

    public void eliminarClave(String codigo) throws RuntimeException{
        if(!this.isPresent(KEY_STRING + codigo))
            throw new RuntimeException("Error...Clave no Encontrada...");
        this.usuarioRedisDTORedisTemplate.delete(KEY_STRING + codigo);
    }

    public Set<String> getKeys(){
        return  usuarioRedisDTORedisTemplate.keys(KEY_STRING + "*");
    }
}
