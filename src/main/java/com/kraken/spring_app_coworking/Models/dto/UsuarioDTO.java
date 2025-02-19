package com.kraken.spring_app_coworking.Models.dto;

import com.kraken.spring_app_coworking.Models.Rol;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsuarioDTO {
    private Long id;
    private String username;
    private String correo;
    private Rol rol;
}
