package com.kraken.spring_app_coworking.Services.impl;

import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.enums.RolEnum;
import com.kraken.spring_app_coworking.Repositories.RolRepository;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class DataLoader implements CommandLineRunner {

    @Value("${app.default.admin.email}")
    private String email;

    @Value("${app.default.admin.password}")
    private String password;

    @Value("${app.default.admin.username}")
    private String username;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Optional<Rol> optionalRol = rolRepository.findByNombre(RolEnum.ADMIN.name());
        if (!optionalRol.isPresent()){
            throw new Exception("Rol no Encontrado");
        }
            Usuario admin = Usuario.builder()
                    .correo(this.email)
                    .password(this.passwordEncoder.encode(this.password))
                    .username(this.username)
                    .rol(optionalRol.get())
                    .build();
            usuarioRepository.save(admin);
        System.out.println("usuario almacenado");
    }
}
