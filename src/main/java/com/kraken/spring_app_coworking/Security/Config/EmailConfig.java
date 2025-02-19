package com.kraken.spring_app_coworking.Security.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {

    @Value("${spring.email.username}")
    private String email;

    @Value("${spring.email.password}")
    private String password;

    //propiedades del servidor de correo que se puede modificar si es de otro proveedor que no es de gmail.com,incluso puede variar de puerto
    private Properties getMailProperties(){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");//requiere correo y contrasenia dada
        properties.put("mail.smtp.starttls" + ".enable","true");//cifrar la conexion inicial
        properties.put("mail.smtp.host" , "smtp" + ".gmail.com");//establecemos proveedor
        properties.put("mail.smtp.port","587");//puerto del servidor SMTP
        //de chatgpt
        properties.put("mail.smtp.ssl.trust","smtp.gmail.com");//cliente confia de gmail.com cuando usa SSL
        properties.put("mail.smtp.ssl.protocols","TLSv1.2");//establece protocolo de seguridad
        properties.put("mail.smtp.connectiontimeout","5000");//establece limite a la conexion con el servidor
        properties.put("mail.smtp.timeout","5000");//establece tiempo maximo de respuesta del servidor
        properties.put("mail.smtp.writetimeout","5000");//establece tiempo maximo para enviar datos al servidor


        return properties;
    }

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(this.getMailProperties());
        mailSender.setUsername(this.email);
        mailSender.setPassword(this.password);
        return mailSender;
    }

    @Bean
    public ResourceLoader resourceLoader(){
        return new DefaultResourceLoader();
    }
}
