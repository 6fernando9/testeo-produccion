package com.kraken.spring_app_coworking.Services.interfaces;


import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Exceptions.ErrorCorreoNoValido;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.dto.CodigoVerificacionDTO;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findUsuarioDTOById(Long id)  throws EntityNotFoundException ;
    UsuarioDTO save(Usuario usuario);
    void deleteById(Long id)  throws EntityNotFoundException;
    Optional<Usuario> findById(Long id);
    UsuarioDTO updateUsuario(Long id, Usuario usuario) throws EntityNotFoundException;
    Usuario findByCorreo(String correo) throws EntityNotFoundException;

    CodigoVerificacionDTO registrarYObtenerCodigoDeVerificacion(Usuario usuario) throws MessagingException, ErrorCorreoNoValido;

    UsuarioDTO confirmarUsuario(CodigoVerificacionDTO codigoVerificacionDTO) throws IllegalArgumentException, EntityNotFoundException;
}
