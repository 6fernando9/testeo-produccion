package com.kraken.spring_app_coworking.Services.impl;

import com.kraken.spring_app_coworking.Email.EmailService;
import com.kraken.spring_app_coworking.Email.dto.EmailCodigoDTO;
import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Exceptions.ErrorCorreoNoValido;
import com.kraken.spring_app_coworking.Mappers.UsuarioMapper;
import com.kraken.spring_app_coworking.Models.Notificacion;
import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.Usuario;
import com.kraken.spring_app_coworking.Models.dto.CodigoVerificacionDTO;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;
import com.kraken.spring_app_coworking.Models.dto.UsuarioRedisDTO;
import com.kraken.spring_app_coworking.Models.enums.NotificacionEnum;
import com.kraken.spring_app_coworking.Models.enums.RolEnum;
import com.kraken.spring_app_coworking.Redis.RedisVerificationCodeService;
import com.kraken.spring_app_coworking.Repositories.RolRepository;
import com.kraken.spring_app_coworking.Repositories.UsuarioRepository;
import com.kraken.spring_app_coworking.Services.interfaces.UsuarioService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.kraken.spring_app_coworking.Redis.RedisVerificationCodeService.KEY_STRING;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisVerificationCodeService redisVerificationCodeService;


    @Override
    public List<UsuarioDTO> findAll() {
        List<Usuario> usuarioList= (List<Usuario>) this.usuarioRepository.findAll();
        List<UsuarioDTO> usuarioDTOList=usuarioList.stream().map(usuario ->
                 UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(usuario))
                .collect(Collectors.toList());
        return usuarioDTOList;
    }

    @Override
    public UsuarioDTO findUsuarioDTOById(Long id) throws EntityNotFoundException{
        Usuario usuario=this.verificarSiExisteUsuario(id);
        UsuarioDTO usuarioDTO=UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(usuario);
        return usuarioDTO;
    }

    //guardara cada usuario por defecto como rol Usuario y no como admin
    @Override
    public UsuarioDTO save(Usuario usuario) {
        System.out.println(RolEnum.USUARIO.name());
        Optional<Rol> optionalRol = this.rolRepository.findByNombre(RolEnum.USUARIO.name());
        optionalRol.ifPresent(rol -> usuario.setRol(rol));
        String passwordEncoded = this.passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncoded);
        return UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(this.usuarioRepository.save(usuario));
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException{
        Optional<Usuario>optionalUsuario=usuarioRepository.findById(id);
        if(!optionalUsuario.isPresent()){
            throw new EntityNotFoundException("Error Usuario no Encontrado");
        }
        this.usuarioRepository.deleteById(id);
    }


    @Override
    public Optional<Usuario> findById(Long id) {
        return this.usuarioRepository.findById(id);
    }

    //metodo para verificar si existe un usuario si existe entonces solamente retorna al objeto usuario
    // si no eixste entonces lanza una excepcion
    private Usuario verificarSiExisteUsuario(Long id) throws EntityNotFoundException{
        Optional<Usuario>optionalUsuario=usuarioRepository.findById(id);
        if(!optionalUsuario.isPresent()){
            throw new EntityNotFoundException("Error Usuario no Encontrado");
        }
        return optionalUsuario.get();
    }

    @Override
    public UsuarioDTO updateUsuario(Long id,@Valid Usuario usuario) throws EntityNotFoundException{
        Optional<Usuario> optionalUsuario= this.usuarioRepository.findById(id);
        if(optionalUsuario.isPresent()){
            Usuario usuario1=optionalUsuario.get();
            usuario1.setCorreo(usuario.getCorreo());
            usuario1.setRol(usuario.getRol());
            usuario1.setUsername(usuario.getUsername());
            usuario1.setPassword(usuario.getPassword());
            return UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(this.usuarioRepository.save(usuario1));
        }
        throw new EntityNotFoundException("Error Usuario no Encontrado..");
    }

    /**
     * @param correo
     * @return
     */
    @Override
    public Usuario findByCorreo(String correo) throws EntityNotFoundException{
        return this.usuarioRepository.findByCorreo(correo).orElseThrow(() -> new EntityNotFoundException());
    }

    /**
     * metodo para enviar el codigo de verificacion al usuario por correo
     * @param usuario
     * @return
     */
    @Override
    public CodigoVerificacionDTO registrarYObtenerCodigoDeVerificacion(Usuario usuario) throws MessagingException, ErrorCorreoNoValido {
        String passwordEncoded = this.passwordEncoder.encode(usuario.getPassword());
        //usuario.setPassword(passwordEncoded);
        String codigo = UUID.randomUUID().toString().replace("-","")
                .substring(0,9).toUpperCase();
        System.out.println("codigo generado: "+codigo);
        UsuarioRedisDTO usuarioRedisDTO = UsuarioRedisDTO.builder()
                .rol(RolEnum.USUARIO.name())
                .password(passwordEncoded)
                .correo(usuario.getCorreo())
                .username(usuario.getUsername())
                .codigo(codigo).build();
        EmailCodigoDTO emailDTO = EmailCodigoDTO.builder()
                .destinatario(usuario.getCorreo())
                .asunto("CODIGO DE CONFIRMACION")
                .codigoConfirmacion(codigo).build();
        //envio de correo
        this.emailService.enviarCodigoDeConfirmacion(emailDTO);//se debe tener en cuenta si el correo no existe
        this.redisVerificationCodeService.guardarCodigo(usuarioRedisDTO);//

        return new CodigoVerificacionDTO(codigo);
    }

    /**
     * metodo para validar el codigo de verificacion al registrar el usuario
     * es aqui donde se valida el codigo introducido y se registra al usuario en la base de datos
     * una vez registrado entonces se debe eliminar todos los codigos asociados a ese email
     * @param codigoVerificacionDTO se toma el codigo de verificacion
     * @return retorna el usuario registrado
     * @throws IllegalArgumentException
     */
    @Override
    public UsuarioDTO confirmarUsuario(CodigoVerificacionDTO codigoVerificacionDTO) throws IllegalArgumentException, EntityNotFoundException {
        String codigoRequest = codigoVerificacionDTO.codigoVerificacion();
        UsuarioRedisDTO usuarioRedisDTO = this.redisVerificationCodeService.getData(KEY_STRING+codigoRequest);
        System.out.println("confirmar usuario "+usuarioRedisDTO);
        //validar si ocurre un error
        if(codigoRequest.equals(usuarioRedisDTO.getCodigo())){
            Optional<Rol> optionalRol = this.rolRepository.findByNombre(usuarioRedisDTO.getRol());
            if(optionalRol.isPresent()){
                Usuario usuario = Usuario.builder()
                        .username(usuarioRedisDTO.getUsername())
                        .correo(usuarioRedisDTO.getCorreo())
                        .password(usuarioRedisDTO.getPassword())
                        .rol(optionalRol.get())
                        .build();
                Notificacion notificacion = Notificacion.builder()
                        .fecha(LocalDateTime.now())
                        .notificacionEnum(NotificacionEnum.CODIGO_REGISTRO).usuario(usuario).build();
                usuario.setNotificaciones(new ArrayList<>());
                usuario.getNotificaciones().add(notificacion);//cuando confirmamos recien registramos la notificacion,esto podria cambiar
                this.redisVerificationCodeService.eliminarClave(codigoRequest);
                return UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(this.usuarioRepository.save(usuario));
             }
            throw new EntityNotFoundException("Error..Rol no encontrado");
        }
        throw new IllegalArgumentException("Error al Confirmar el codigo..");

    }
}
