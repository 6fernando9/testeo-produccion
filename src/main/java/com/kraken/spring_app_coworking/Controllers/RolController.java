package com.kraken.spring_app_coworking.Controllers;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.Permiso;
import com.kraken.spring_app_coworking.Models.Rol;
import com.kraken.spring_app_coworking.Models.dto.UsuarioDTO;
import com.kraken.spring_app_coworking.Services.interfaces.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    @Autowired
    private RolService rolService;
    @PostMapping("/save")
    public ResponseEntity<?> saveRol(@RequestBody Rol rol){
        return ResponseEntity.ok(this.rolService.save(rol));
    }
    @GetMapping
    public ResponseEntity<?> findAllRoles(){
        return ResponseEntity.ok(this.rolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findRolById(@PathVariable  Long id) throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.rolService.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRol(@PathVariable Long id, @RequestBody Rol rol) throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.rolService.updateRol(id,rol));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRolById(@PathVariable Long id) throws EntityNotFoundException {
        this.rolService.deleteById(id);
    }

    //controlador para asignar permisos a un rol
    @PostMapping("/{id}/permisos")
    public ResponseEntity<Set<Permiso>> asignarPermisosPorRol(@PathVariable Long id
            , @RequestBody List<Permiso> permisosList) throws EntityNotFoundException {
        Set<Permiso> permisosSet = this.rolService.asignarPermisosPorRol(id,permisosList);
        return ResponseEntity.ok(permisosSet);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> obtenerUsuariosPorNombreRol(@RequestParam String rolname){
        List<UsuarioDTO> usuarioDTOList =this.rolService.getUsuariosByRolName(rolname);
        return ResponseEntity.ok(usuarioDTOList);
    }

    @GetMapping("/usuarios2")
    public ResponseEntity<?> obtenerUsuariosPorNombreRol2(@RequestParam String rolname){
        List<UsuarioDTO> usuarioDTOList =this.rolService.getUsuariosByRolNames(rolname);
        return ResponseEntity.ok(usuarioDTOList);
    }
}
