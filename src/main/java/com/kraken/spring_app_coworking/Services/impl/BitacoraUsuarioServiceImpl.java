package com.kraken.spring_app_coworking.Services.impl;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.BitacoraUsuario;
import com.kraken.spring_app_coworking.Models.dto.BitacoraUsuarioDTO;
import com.kraken.spring_app_coworking.Models.enums.BitacoraUsuarioEnum;
import com.kraken.spring_app_coworking.Repositories.BitacoraUsuarioRepository;
import com.kraken.spring_app_coworking.Services.interfaces.BitacoraUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BitacoraUsuarioServiceImpl implements BitacoraUsuarioService {

    @Autowired
    private BitacoraUsuarioRepository bitacoraUsuarioRepository;

    /**
     * @param id recibe el id de la bitacora, aunque quizas no se ocupe
     * @return retorna un objeto bitacora
     */
    @Override
    public Optional<BitacoraUsuario> findById(Long id) {
        return this.bitacoraUsuarioRepository.findById(id);
    }

    /**
     * @param username le mando el username del usuario
     * @return retorna un dto
     */
    @Override
    public List<BitacoraUsuarioDTO> findByUsername(String username) {
        List<BitacoraUsuario> bitacoraUsuario = this.bitacoraUsuarioRepository.findByUsername(username);
        List<BitacoraUsuarioDTO> bitacoraUsuarioDTOList = this.obtenerListaBitacoraDTO(bitacoraUsuario);
        return bitacoraUsuarioDTOList;
    }

    private void setearBitacoraDto(BitacoraUsuario bitacoraUsuario, BitacoraUsuarioDTO bitacoraUsuarioDTO) {

        bitacoraUsuarioDTO.setId(bitacoraUsuario.getId());
        bitacoraUsuarioDTO.setUsuarioId(String.valueOf(bitacoraUsuario.getUsuarioId()));
        bitacoraUsuarioDTO.setUsername(bitacoraUsuario.getUsername());
        bitacoraUsuarioDTO.setIp(bitacoraUsuario.getIp());
        String tipoSexion = bitacoraUsuario.getTipoSesion().equals(BitacoraUsuarioEnum.INICIO_DE_SESION.getAbreviacion()) ? BitacoraUsuarioEnum.INICIO_DE_SESION.name()
                : bitacoraUsuario.getTipoSesion().equals(BitacoraUsuarioEnum.CIERRE_DE_SESION.getAbreviacion()) ? BitacoraUsuarioEnum.CIERRE_DE_SESION.name() : BitacoraUsuarioEnum.REFRESH_TOKEN.name();
        bitacoraUsuarioDTO.setTipoSesion(tipoSexion);
        LocalDateTime localDateTime = bitacoraUsuario.getFecha();
        bitacoraUsuarioDTO.setHora(localDateTime.toLocalTime());
        bitacoraUsuarioDTO.setFecha(localDateTime.toLocalDate());
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<BitacoraUsuarioDTO> findByUsuarioId(Long id) {
        List<BitacoraUsuario> bitacoraUsuario = this.bitacoraUsuarioRepository.findByUsuarioId(id);
        List<BitacoraUsuarioDTO> bitacoraUsuarioDTOList = this.obtenerListaBitacoraDTO(bitacoraUsuario);
        return bitacoraUsuarioDTOList;
    }

    /**
     * @return
     */
    @Override
    public List<BitacoraUsuarioDTO> findAll() {
        List<BitacoraUsuario> bitacoraUsuarioList = this.bitacoraUsuarioRepository.findAll();
        return this.obtenerListaBitacoraDTO(bitacoraUsuarioList);
    }


    private List<BitacoraUsuarioDTO> obtenerListaBitacoraDTO(List<BitacoraUsuario> bitacoraUsuarioList){
        List<BitacoraUsuarioDTO> bitacoraUsuarioDTOList = bitacoraUsuarioList.stream()
                .map(bitacoraUsuario -> {
                    BitacoraUsuarioDTO bitacoraUsuarioDTO = new BitacoraUsuarioDTO();
                    this.setearBitacoraDto(bitacoraUsuario,bitacoraUsuarioDTO);
                    return bitacoraUsuarioDTO;
                }).collect(Collectors.toList());
        return bitacoraUsuarioDTOList;
    }
    /**
     * @param bitacoraUsuario
     * @return
     */
    @Override
    public BitacoraUsuarioDTO save(BitacoraUsuario bitacoraUsuario) {
        BitacoraUsuarioDTO bitacoraUsuarioDTO = new BitacoraUsuarioDTO();
        this.setearBitacoraDto(bitacoraUsuario,bitacoraUsuarioDTO);
        this.bitacoraUsuarioRepository.save(bitacoraUsuario);
        return bitacoraUsuarioDTO;
    }
}
