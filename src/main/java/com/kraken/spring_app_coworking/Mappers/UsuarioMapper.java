package com.kraken.spring_app_coworking.Mappers;

import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
    UsuarioDTO usuarioToUsuarioDTO(Usuario usuario);
    List<UsuarioDTO> usuarioToUsuarioDTOList(List<Usuario> usuarioList);
    Usuario usuarioDTOtoUsuario(UsuarioDTO usuarioDTO);
}
