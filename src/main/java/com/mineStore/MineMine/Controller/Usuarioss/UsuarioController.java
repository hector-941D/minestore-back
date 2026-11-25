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

import com.mineStore.MineMine.DTO.Usuarioss.UsuarioDTO;
import com.mineStore.MineMine.Model.Usuarioss.Usuario;
import com.mineStore.MineMine.Service.Usuarioss.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuarios", description = "Operaciones CRUD para Usuarios")
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve la lista completa de usuarios registrados")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @Operation(summary = "Buscar usuario por ID", description = "Devuelve un usuario segun su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontro el usuario", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar un usuario", description = "Registra un nuevo usuario")
    @PostMapping
    public ResponseEntity<?> guardarUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            UsuarioDTO guardado = usuarioService.guardarUsuario(usuario);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar el usuario, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un usuario", description = "Actualiza un usuario buscandolo por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuario));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontro el usuario", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un usuario", description = "Borra un usuario utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id) {
        String resultado = usuarioService.eliminarUsuario(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}