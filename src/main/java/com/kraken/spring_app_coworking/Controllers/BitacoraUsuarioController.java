package com.kraken.spring_app_coworking.Controllers;

import com.kraken.spring_app_coworking.Services.interfaces.BitacoraUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bitacoras")
public class BitacoraUsuarioController {

    @Autowired
    private BitacoraUsuarioService bitacoraUsuarioService;

    @GetMapping("/username")
    public ResponseEntity<?> findByUsername(@RequestParam String username){
        return ResponseEntity.ok(this.bitacoraUsuarioService.findByUsername(username));
    }

    @GetMapping("/id-usuario/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return ResponseEntity.ok(this.bitacoraUsuarioService.findByUsuarioId(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(this.bitacoraUsuarioService.findAll());
    }
}
