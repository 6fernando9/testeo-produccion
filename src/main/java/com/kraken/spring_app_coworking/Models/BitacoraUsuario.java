package com.kraken.spring_app_coworking.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bitacora_usuarios")
public class BitacoraUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotNull
    @NotBlank
    private String username;

    @Column(name = "tipo_sesion" , length = 1)
    private String tipoSesion;

    @NotNull
    @NotBlank
    private String ip;

    private LocalDateTime fecha;

}
