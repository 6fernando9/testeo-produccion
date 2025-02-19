package com.kraken.spring_app_coworking.Services.interfaces;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.BitacoraUsuario;
import com.kraken.spring_app_coworking.Models.dto.BitacoraUsuarioDTO;

import java.util.List;
import java.util.Optional;

public interface BitacoraUsuarioService {

    Optional<BitacoraUsuario> findById(Long id);

    List<BitacoraUsuarioDTO> findByUsername(String username) ;

    List<BitacoraUsuarioDTO> findByUsuarioId(Long id) ;

    List<BitacoraUsuarioDTO> findAll();

    BitacoraUsuarioDTO save(BitacoraUsuario bitacoraUsuario);

}
