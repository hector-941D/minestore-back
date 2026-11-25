package com.mineStore.MineMine.Controller.Ventass;

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

import com.mineStore.MineMine.DTO.Ventass.TipoDTO;
import com.mineStore.MineMine.Model.Ventass.Tipo;
import com.mineStore.MineMine.Service.Ventass.TipoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Tipos", description = "Operaciones CRUD para Tipos de Documento")
@RestController
@RequestMapping("/api/v1/tipos")
public class TipoController {

    @Autowired
    private TipoService tipoService;

    @Operation(summary = "Obtener todos los tipos", description = "Devuelve la lista completa de tipos registrados")
    @GetMapping
    public ResponseEntity<List<TipoDTO>> obtenerTodosTipos() {
        return ResponseEntity.ok(tipoService.obtenerTodos());
    }

    @Operation(summary = "Buscar tipo por ID", description = "Devuelve un tipo según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(tipoService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontro el tipo", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar un tipo", description = "Registra un nuevo tipo")
    @PostMapping
    public ResponseEntity<?> guardarTipo(@Valid @RequestBody Tipo tipo) {
        try {
            TipoDTO guardado = tipoService.guardarTipo(tipo);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar el tipo, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un tipo", description = "Actualiza un tipo buscandolo por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTipo(@PathVariable Integer id, @Valid @RequestBody Tipo tipo) {
        try {
            return ResponseEntity.ok(tipoService.actualizarTipo(id, tipo));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontro el tipo", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un tipo", description = "Borra un tipo utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTipo(@PathVariable Integer id) {
        String resultado = tipoService.eliminarTipo(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}