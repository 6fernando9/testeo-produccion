package com.kraken.spring_app_coworking.Controllers;


import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Exceptions.ErrorCorreoNoValido;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.dto.CodigoVerificacionDTO;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;
import com.kraken.spring_app_coworking.Redis.RedisVerificationCodeService;
import com.kraken.spring_app_coworking.Security.impl.AuthService;
import com.kraken.spring_app_coworking.Services.interfaces.UsuarioService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthService authService;


    @GetMapping()
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(this.usuarioService.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Valid @RequestBody Usuario usuario) throws EntityNotFoundException {
        // System.out.println(id);

        UsuarioDTO usuarioDTO = this.usuarioService.updateUsuario(id, usuario);
        return ResponseEntity.ok(usuarioDTO);
    }
    //con Pathvariable
    //para llamar desde el cliente localhost:8000/api/usuarios/1
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> findUserById(@PathVariable( name = "idUsuario") Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(this.usuarioService.findUsuarioDTOById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) throws EntityNotFoundException {
        this.usuarioService.deleteById(id);
        //return ResponseEntity.ok();
    }

    @GetMapping("/prueba")
    //@PreAuthorize("hasRole('USUARIO')") // por defecto si solo se usa esto y no lo colocas en el config,no funciona
    public ResponseEntity<?> hola() {
        //Long userId = authService.getAuthenticatedUser();
        return ResponseEntity.ok("sdasdsdadas");
    }




/// pruebas
    @GetMapping("/check-authorities")
    @PreAuthorize("hasRole('USUARIO')")  // Añade esta línea
    public ResponseEntity<?> checkAuthorities(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("authorities", authentication.getAuthorities());
        response.put("principal", authentication.getPrincipal());
        response.put("isAuthenticated", authentication.isAuthenticated());
        response.put("requestURL", "Este endpoint requiere ROLE_USUARIO");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-authorities-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> checkAuthoritiesAdmin(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("authorities", authentication.getAuthorities());
        response.put("principal", authentication.getPrincipal());
        response.put("isAuthenticated", authentication.isAuthenticated());
        response.put("requestURL", "Este endpoint requiere ROLE_ADMIN");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/registrar/obtener-codigo")
    public ResponseEntity<?> registrarConCodigo(@RequestBody Usuario usuario) throws MessagingException, ErrorCorreoNoValido {
        CodigoVerificacionDTO jsonResponse = this.usuarioService.registrarYObtenerCodigoDeVerificacion(usuario);
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/registrar/enviar-codigo")
    public ResponseEntity<?> enviarCodigoYObtenerNotificacion(@RequestBody CodigoVerificacionDTO jsonRequest) throws IllegalArgumentException,EntityNotFoundException {
        UsuarioDTO usuarioDTO = this.usuarioService.confirmarUsuario(jsonRequest);
        return ResponseEntity.ok(usuarioDTO);
    }

    @Autowired
    private RedisVerificationCodeService redisService;

    @GetMapping("/registrar/redis/getKeys")
    public ResponseEntity<?> checkKey() {
        return ResponseEntity.ok(this.redisService.getKeys());
    }
}
