package com.kraken.spring_app_coworking.Email;

import com.kraken.spring_app_coworking.Email.dto.EmailCodigoDTO;
import com.kraken.spring_app_coworking.Email.dto.EmailDTO;
import com.kraken.spring_app_coworking.Exceptions.ErrorCorreoNoValido;
import jakarta.mail.MessagingException;

public interface EmailService {
     void enviarCorreo(EmailDTO emailDTO) throws MessagingException;
     void enviarCodigoDeConfirmacionPrueba(EmailDTO emailDTO) throws MessagingException;
     void enviarCodigoDeConfirmacion(EmailCodigoDTO emailCodigoDTO) throws MessagingException, ErrorCorreoNoValido;
     boolean esCorreoValido(String correo);
}
