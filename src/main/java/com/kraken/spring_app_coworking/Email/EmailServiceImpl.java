package com.kraken.spring_app_coworking.Email;

import com.kraken.spring_app_coworking.Email.dto.EmailCodigoDTO;
import com.kraken.spring_app_coworking.Email.dto.EmailDTO;
import com.kraken.spring_app_coworking.Exceptions.ErrorCorreoNoValido;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    @Override
    public void enviarCorreo(EmailDTO emailDTO) throws MessagingException {
        try{
            MimeMessage message = this.javaMailSender.createMimeMessage();
            //multipart true por si se quiere enviar archivos adjuntos o contenido HTML por correo
            MimeMessageHelper helper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.name());
            helper.setTo(emailDTO.getDestinatario());
            helper.setSubject(emailDTO.getAsunto());
            helper.setText(emailDTO.getMessage());
            javaMailSender.send(message);
            //tambien podemos enviar archivos
            // mimeMessageHelper.addAttachment(file.getName(), file);
            //mas ejemplos
//            helper.setText("<h1>Hola, este es un mensaje con formato HTML</h1>", true); // true para HTML
//            helper.addAttachment("archivo.pdf", new File("ruta/del/archivo.pdf"));
        }catch (Exception e){
            throw new RuntimeException("Error al enviar el correo",e);
        }
    }

//    public void enviarCodigoDeConfirmacion(EmailCodigoDTO emailDTO){
//        try{
//            MimeMessage message = this.javaMailSender.createMimeMessage();
//            String codigoConfirmacion= UUID.randomUUID().toString();
//            codigoConfirmacion = codigoConfirmacion.substring(7);
//            System.out.println(codigoConfirmacion);
//            MimeMessageHelper helper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.name());
//            helper.setTo(emailDTO.getDestinatario());
//            helper.setSubject(emailDTO.getAsunto());
//            helper.setText(emailDTO.getMessage());
//            javaMailSender.send(message);
//        }catch (Exception e){
//            throw new RuntimeException("Error al enviar el correo",e);
//        }
//    }
    @Override
    public void enviarCodigoDeConfirmacionPrueba(EmailDTO emailDTO) throws MessagingException{
        try {
            MimeMessage message = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            // Generar código de confirmación (7 caracteres)
            String codigoConfirmacion = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
            System.out.println("Código de confirmación: " + codigoConfirmacion);

            // Crear cuerpo del correo con HTML
            String contenidoHtml = "<html>" +
                    "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                    "<h2 style='color: #6441a5;'>Código de Confirmación</h2>" +
                    "<p>Gracias por registrarte en nuestro servicio. Tu código de confirmación es:</p>" +
                    "<h1 style='color: #6441a5; background-color: #f4f4f4; padding: 10px; display: inline-block;'>" + codigoConfirmacion + "</h1>" +
                    "<p>Por favor, ingrésalo en la plataforma para completar el proceso.</p>" +
                    "<br>" +
                    "<p><strong>¡Gracias por confiar en nosotros!</strong></p>" +
                    "</body>" +
                    "</html>";

            // Configurar destinatario, asunto y contenido HTML
            helper.setTo(emailDTO.getDestinatario());
            helper.setSubject(emailDTO.getAsunto());
            helper.setText(contenidoHtml, true); // 'true' indica que el texto es HTML

            // Adjuntar imagen (hiphopfree.jpg) y PDF (horario_guardaparque (8).pdf)
            ///ClassPathResource classPathResource =  new ClassPathResource("testEmail/hiphopfree.jpg");

            helper.addAttachment("imagen.jpg", new FileSystemResource(new File("testEmail/hiphopfree.jpg")));
            helper.addAttachment("documento.pdf", new FileSystemResource(new File("testEmail/horario_guardaparque (8).pdf")));


            // Enviar el correo
            javaMailSender.send(message);
            System.out.println("Correo enviado con éxito a " + emailDTO.getDestinatario());

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }

    /**
     * @param emailCodigoDTO
     * @throws MessagingException
     */
    @Override
    public void enviarCodigoDeConfirmacion(EmailCodigoDTO emailCodigoDTO) throws MessagingException, ErrorCorreoNoValido {
        try {
            MimeMessage message = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            // Configurar destinatario, asunto y contenido HTML
            if(!this.esCorreoValido(emailCodigoDTO.getDestinatario())){
                throw new ErrorCorreoNoValido();
            }
            // Crear cuerpo del correo con HTML
            String contenidoHtml = "<html>" +
                    "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                    "<h2 style='color: #6441a5;'>Código de Confirmación</h2>" +
                    "<p>Gracias por registrarte en nuestro servicio. Tu código de confirmación es:</p>" +
                    "<h1 style='color: #6441a5; background-color: #f4f4f4; padding: 10px; display: inline-block;'>" + emailCodigoDTO.getCodigoConfirmacion() + "</h1>" +
                    "<p>Por favor, ingrésalo en la plataforma para completar el proceso.</p>" +
                    "<br>" +
                    "<p><strong>¡Gracias por confiar en nosotros!</strong></p>" +
                    "</body>" +
                    "</html>";

            helper.setTo(emailCodigoDTO.getDestinatario());
            helper.setSubject(emailCodigoDTO.getAsunto());
            helper.setText(contenidoHtml, true); // 'true' indica que el texto es HTML

            javaMailSender.send(message);
            System.out.println("Correo enviado con éxito a " + emailCodigoDTO.getDestinatario());

        } catch (Exception e) {
            //throw new RuntimeException("Error al enviar el correo", e);
            throw new ErrorCorreoNoValido("No se pudo enviar el correo. Verifica la dirección.");
        }
    }

    /**
     * @param correo
     * @return
     */
    @Override
    public boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return correo.matches(regex);
    }
}
