package com.kraken.spring_app_coworking.Security.Config;

import com.kraken.spring_app_coworking.Models.enums.RolEnum;
import com.kraken.spring_app_coworking.Repositories.BitacoraUsuarioRepository;
import com.kraken.spring_app_coworking.Security.Filters.JwtAuthenticationFilter;
import com.kraken.spring_app_coworking.Security.Filters.JwtValidationFilter;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import com.kraken.spring_app_coworking.Security.impl.CustomOAuth2UserService;
import com.kraken.spring_app_coworking.Security.interfaces.JWTTokenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private UsuarioRepository usuarioRepository;

//    @Autowired
//    private CorsConfig corsConfigurationSource; // Inyecta el bean del CORS

    @Autowired
    private JWTTokenService jwtTokenService;


    @Autowired
    private BitacoraUsuarioRepository bitacoraUsuarioRepository;

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return this.authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    //declaro un bean para usarlo luego
//    @Bean
//    JWTTokenService jwtTokenService(){
//        return new JWTTokenServiceImpl();
//    }

    // ✅ Registra JwtAuthenticationFilter como un Bean para que Spring maneje la inyección de dependencias
    //asi en el filter no instanciamos el objeto, si no que usamos la inyeccion de dependencias de spring
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//        return new JwtAuthenticationFilter(this.authenticationManager(), usuarioRepository, jwtTokenService);
//    }




    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.authorizeHttpRequests((authz) -> authz
                        .requestMatchers( "/auth/**").permitAll()
                        .requestMatchers("/api/servicios/**").permitAll()
                        .requestMatchers("/api/bitacoras/**").permitAll()
                        .requestMatchers("/api/email/**").permitAll()
                        .requestMatchers("/api/usuarios/registrar/**").permitAll()//prueba envio de mensaje con correo, y guardado de notificacion
                        .requestMatchers(HttpMethod.POST,"/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/check-authorities").hasRole(RolEnum.USUARIO.name())
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/check-authorities-admin").hasRole(RolEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,"/api/usuarios/prueba").hasRole(RolEnum.USUARIO.name())
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/update/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USUARIO")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/{idUsuario}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/{id}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/usuarios/getId").hasAuthority("ROLE_USUARIO")
                        .requestMatchers("/auth/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(this.authenticationManager(),this.usuarioRepository,this.jwtTokenService,this.bitacoraUsuarioRepository))//usa el bean en lugar del new
                .addFilter(new JwtValidationFilter(authenticationManager(),this.jwtTokenService))
                .oauth2Login(oauth2 -> oauth2
                    //.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService) )
                                .successHandler(((request, response, authentication) -> {
                                    response.sendRedirect("/auth/oauth2/success");

                                }))
                    .failureHandler((request, response, exception) -> {
                                    System.out.println("Error de autenticación: " + exception.getMessage());
                                    response.sendRedirect("/auth/oauth2/failure");
                                })
                )
                .csrf(config -> config.disable())//stateless = sin estado
    //            .cors(cors-> cors.configurationSource(this.corsConfigurationSource.corsConfigurationSource()))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))//si es stateless no funca el oauth
                .build();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        return CorsConfig.(); // Llama al método estático de CorsConfig
//    }


}
