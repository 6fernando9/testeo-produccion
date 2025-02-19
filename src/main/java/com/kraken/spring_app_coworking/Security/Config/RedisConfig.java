package com.kraken.spring_app_coworking.Security.Config;

import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.dto.UsuarioRedisDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    //LettuceConnectionFactory connectionFactory es una fabrica de conexiones que spring boot crea automatico a partir de
    // spring.redis.host y spring.redis.port
    //el redisTemplate permite guardar y recuperar datos de redis
    @Bean
    public RedisTemplate<String,String> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, UsuarioRedisDTO> redisRegisterTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, UsuarioRedisDTO> template = new RedisTemplate<>();

        // Configura la fábrica de conexiones
        template.setConnectionFactory(connectionFactory);

        // Serializador para las claves (String)
        template.setKeySerializer(new StringRedisSerializer());

        // Serializador para los valores (UsuarioRedisDTO)
        Jackson2JsonRedisSerializer<UsuarioRedisDTO> serializer = new Jackson2JsonRedisSerializer<>(UsuarioRedisDTO.class);
        template.setValueSerializer(serializer);

        // Serializador para las claves hash (opcional, si usas operaciones hash)
        template.setHashKeySerializer(new StringRedisSerializer());

        // Serializador para los valores hash (opcional, si usas operaciones hash)
        template.setHashValueSerializer(serializer);

        return template;
    }


}
