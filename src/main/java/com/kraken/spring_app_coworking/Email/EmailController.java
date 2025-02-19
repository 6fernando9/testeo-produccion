package com.kraken.spring_app_coworking.Email;

import com.kraken.spring_app_coworking.Email.dto.EmailCodigoDTO;
import com.kraken.spring_app_coworking.Email.dto.EmailDTO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    private ResponseEntity<String> enviarEmail(@RequestBody EmailDTO emailDTO) throws MessagingException {
        this.emailService.enviarCorreo(emailDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Email Enviado con Exito");
    }
    @PostMapping("/send")
    private ResponseEntity<String> enviarEmail2() throws MessagingException {
        //this.emailService.enviarCorreo(emailDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Email Enviado con Exito");
    }

    @PostMapping("/send-email-modi")
    private ResponseEntity<String> enviarEmailConPdfYImagenYHTML(@RequestBody EmailDTO emailDTO) throws MessagingException {
        this.emailService.enviarCodigoDeConfirmacionPrueba(emailDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Email Enviado con Exito");
    }
}
