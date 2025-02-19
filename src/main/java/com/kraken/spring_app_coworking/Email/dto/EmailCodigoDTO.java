package com.kraken.spring_app_coworking.Email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCodigoDTO {
    private String destinatario;//obligatorio, indica a que correo debe ir
    private String asunto; //como el titulo del correo
    private String message; //mensaje del correo
    private String codigoConfirmacion;
}
