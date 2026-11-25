package com.mineStore.MineMine.Controller.Usuarioss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mineStore.MineMine.DTO.Usuarioss.RolDTO;
import com.mineStore.MineMine.Model.Usuarioss.Rol;
import com.mineStore.MineMine.Service.Usuarioss.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Roles", description = "Operaciones CRUD para Roles")
@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @Operation(summary = "Obtener todos los roles", description = "Devuelve la lista completa de roles registrados")
    @GetMapping
    public ResponseEntity<List<RolDTO>> obtenerTodosRoles() {
        return ResponseEntity.ok(rolService.obtenerTodos());
    }

    @Operation(summary = "Buscar rol por ID", description = "Devuelve un rol segun su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(rolService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontro el rol", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar un rol", description = "Registra un nuevo rol")
    @PostMapping
    public ResponseEntity<?> guardarRol(@Valid @RequestBody Rol rol) {
        try {
            RolDTO guardado = rolService.guardarRol(rol);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar el rol, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un rol", description = "Actualiza un rol buscandolo por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRol(@PathVariable Integer id, @Valid @RequestBody Rol rol) {
        try {
            return ResponseEntity.ok(rolService.actualizarRol(id, rol));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontró el rol", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un rol", description = "Borra un rol utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRol(@PathVariable Integer id) {
        String resultado = rolService.eliminarRol(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}