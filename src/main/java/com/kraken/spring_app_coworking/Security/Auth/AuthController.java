package com.kraken.spring_app_coworking.Security.Auth;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Security.Auth.dto.RequestRefreshJWT;
import com.kraken.spring_app_coworking.Security.interfaces.JWTTokenService;
import com.kraken.spring_app_coworking.Services.interfaces.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.kraken.spring_app_coworking.Security.Auth.TokenJwtConfig.PREFIX_TOKEN;
import static com.kraken.spring_app_coworking.Security.Auth.TokenJwtConfig.HEADER_AUTHORIZATION;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RequestRefreshJWT request, HttpServletRequest httpRequest) throws JwtException, ExpiredJwtException, EntityNotFoundException {
        //valido el token
        Claims claims = this.jwtTokenService.getClaimsToken(request.getRefreshToken());
        Long id = Long.parseLong(claims.getSubject());

        // si no lo encuentra entonces retorna una excepcion
        Usuario usuario = this.usuarioService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        Rol rol = usuario.getRol();
        List<String> roles = new ArrayList<>();
        roles.add(rol.getNombre());
        List<String> permisos = new ArrayList<>();
        rol.getPermisos().stream().forEach(permiso -> permisos.add(permiso.getNombre()));

        String token = this.jwtTokenService.generateAccessToken(usuario, roles, permisos);

        System.out.println(httpRequest.getLocalAddr());
        System.out.println(httpRequest.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
    //login se sobre entiende, que bajo esta implementacion,lo tiene por defecto

    //registrar a un usuario por primera vez
    @PostMapping("/registrar")
    public ResponseEntity<?> signIn(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(this.usuarioService.save(usuario));
    }

    //los datos de estos ya estan cargados, se podria hacer pero por ahora no
    //dado que en el success del filtro cargo un dto, lo que se podria hacer es delegar el metodo aqui y toda la logica vendria a este metodo
    // pero por ahora dejemoslo asi,lo bueno es que ya sabemos como hacerlo si pidiesen modificarlo
//    @GetMapping("/login/success")
//    public ResponseEntity<?> login(Authentication authentication,HttpServletRequest request, HttpServletResponse response){
//        System.out.println(request.getAttribute("loginResponseDTO"));
//        return ResponseEntity.ok("usuario logeado con exito");
//    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(HEADER_AUTHORIZATION) String token
            , HttpServletRequest httpRequest,  Authentication authentication
            ) {
        if (token.startsWith(PREFIX_TOKEN))
            token = token.replace(PREFIX_TOKEN, "");
        // Invalidar el token en Redis con el mismo tiempo de expiración que el JWT
        jwtTokenService.invalidarJWT(token);
        System.out.println();
        System.out.println(httpRequest.getLocalAddr());
        System.out.println(httpRequest.getRemoteAddr());
        return ResponseEntity.ok("Logout exitoso");
    }
}
