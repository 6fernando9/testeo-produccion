package com.kraken.spring_app_coworking.Repositories;

import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario,Long> {
//    @Query("""
//            select u
//            from Usuario u, Rol r
//            where u.rol_id=r.id and rol.nombre=?1
//            """)
//
//    List<Usuario> findUsersByRol(String rolname);

//    @Query("SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.username = :username")
//    List<Usuario> findByUsernameWithRol(@Param("username") String username);

    //carga con eager esta consulta para tener los roles y asi no realize doble consulta
//    @EntityGraph(attributePaths = {"rol"})
    //traeremos a un objeto rol con los usuarios al llamar a esta consulta

    Optional<Usuario> findByCorreo(String correo);
}
