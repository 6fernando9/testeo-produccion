package com.kraken.spring_app_coworking.Models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BitacoraUsuarioDTO {
    private Long id;
    private String usuarioId;
    private String username;
    private String ip;
    private String tipoSesion;
    private LocalDate fecha;
    private LocalTime hora;
}
