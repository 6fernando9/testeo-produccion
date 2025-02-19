package com.kraken.spring_app_coworking.Controllers;

import com.kraken.spring_app_coworking.Exceptions.EntityNotFoundException;
import com.kraken.spring_app_coworking.Models.Permiso;
import com.kraken.spring_app_coworking.Services.interfaces.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/permisos")
public class PermisoController {
    @Autowired
    private PermisoService permisoService;

    @GetMapping
    public Set<Permiso> findAllPermisos(){
        return permisoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findPermisoById(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(this.permisoService.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePermiso(@PathVariable Long id, @RequestBody Permiso permiso) throws EntityNotFoundException{
        return ResponseEntity.ok(this.permisoService.updatePermiso(id,permiso));
    }
    @DeleteMapping("/{id}")
    public void deletePermiso(@PathVariable Long id) throws EntityNotFoundException {
        this.permisoService.deleteById(id);
    }
    //probando la paginacion
    @GetMapping("/paginados ")
    public ResponseEntity<?> getPermisosPaginados(Pageable pageable){
        return ResponseEntity.ok(this.permisoService.findAllPermisosPaginado(pageable));
    }
}
