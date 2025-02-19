package com.kraken.spring_app_coworking.Controllers;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Exceptions.ErrorCorreoNoValido;
import com.kraken.spring_app_coworking.Exceptions.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class HandlerExceptionController {
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Error> errorEntidadNoEncontrada(Exception e){
        Error error=Error.builder()
                .date(new Date())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .descripcion("Error Entidad No Encontrada..")
                .mensaje(e.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(error);
    }
    @ExceptionHandler({ErrorCorreoNoValido.class})
    public ResponseEntity<Error> errorCorreoNoValido(Exception e){
        Error error=Error.builder()
                .date(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .descripcion("Error Correo No Valido..")
                .mensaje(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(error);
    }
}
