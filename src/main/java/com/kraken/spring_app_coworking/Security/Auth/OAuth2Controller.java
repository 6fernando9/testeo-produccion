package com.kraken.spring_app_coworking.Security.Auth;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Mappers.UsuarioMapper;
import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;
import com.kraken.spring_app_coworking.Models.enums.RolEnum;
import com.kraken.spring_app_coworking.Repositories.RolRepository;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import com.kraken.spring_app_coworking.Security.Auth.dto.LoginResponseDTO;
import com.kraken.spring_app_coworking.Security.impl.AuthService;
import com.kraken.spring_app_coworking.Security.interfaces.JWTTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2Controller {
    @Autowired
    private JWTTokenService jwtTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    //si los datos proporcionados en oauth2 fueron correctos entonces va funcionar
    @GetMapping("/success")
    public ResponseEntity<?> oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User, HttpServletRequest request){
        try{
            String email = oauth2User.getAttribute("email");
            String username = oauth2User.getAttribute("name");
            System.out.println(oauth2User);
            System.out.println(request.getRemoteAddr());
            //System.out.println(Optional.ofNullable(oauth2User.getAttribute("picture"))); para agarrar la imagen
            System.out.println(email);
            // Busca o crea al usuario en tu base de datos
            Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(email);
            if(optionalUsuario.isPresent()){
                Usuario usuario = optionalUsuario.get();
                List<String> roles = new ArrayList<>();
                roles.add(usuario.getRol().getNombre());
                List<String> permisos = usuario.getRol().getPermisos().stream().map(permiso -> {
                    return permiso.getNombre();
                }).collect(Collectors.toList());
                // Genera un JWT para el usuario
                String token = jwtTokenService.generateAccessToken(usuario,roles,permisos);
                String refreshToken = jwtTokenService.generateRefreshToken(usuario);
                LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                        token,
                        refreshToken,
                        usuario.getUsername(),
                        String.format("Bienvenido %s has iniciado sesion con exito",usuario.getUsername()));
                return ResponseEntity.ok(loginResponseDTO);
            }

            Rol rol = this.rolRepository.findByNombre(RolEnum.USUARIO.name()).get();
            System.out.println(rol);
            //si no
            Usuario usuario = Usuario.builder().correo(email).username(username).rol(rol).build();
            System.out.println(usuario);
            Usuario usuario1 = this.usuarioRepository.save(usuario);
            UsuarioDTO usuarioDTO = UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(usuario1);
            return ResponseEntity.ok(usuarioDTO);
        }catch (Exception e){
            throw new RuntimeException("Hubo un error inesperado..revisar oauth",e);
        }
    }

    @GetMapping("/failure")
    public ResponseEntity<?> oauth2Failure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error en la autenticación con Google");
    }
}
