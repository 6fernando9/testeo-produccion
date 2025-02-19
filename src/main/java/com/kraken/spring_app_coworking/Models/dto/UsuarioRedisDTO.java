package com.kraken.spring_app_coworking.Models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsuarioRedisDTO implements Serializable {
    private String username;
    private String correo;
    private String password;
    private String codigo;
    private String rol;

}
