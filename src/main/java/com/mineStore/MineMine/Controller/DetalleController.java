package com.mineStore.MineMine.Controller;

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

import com.mineStore.MineMine.DTO.DetalleDTO;
import com.mineStore.MineMine.Model.Detalle;
import com.mineStore.MineMine.Service.DetalleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Detalles", description = "Operaciones CRUD para Detalles de Venta")
@RestController
@RequestMapping("/api/v1/detalles")
public class DetalleController {

    @Autowired
    private DetalleService detalleService;

    @Operation(summary = "Obtener todos los detalles", description = "Devuelve la lista completa de detalles registrados")
    @GetMapping
    public ResponseEntity<List<DetalleDTO>> obtenerTodosDetalles() {
        return ResponseEntity.ok(detalleService.obtenerTodos());
    }

    @Operation(summary = "Buscar detalle por ID", description = "Devuelve un detalle según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(detalleService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontro el detalle", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar un detalle", description = "Registra un nuevo detalle de venta")
    @PostMapping
    public ResponseEntity<?> guardarDetalle(@Valid @RequestBody Detalle detalle) {
        try {
            DetalleDTO guardado = detalleService.guardarDetalle(detalle);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar el detalle, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un detalle", description = "Actualiza un detalle buscándolo por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDetalle(@PathVariable Integer id, @Valid @RequestBody Detalle detalle) {
        try {
            return ResponseEntity.ok(detalleService.actualizarDetalle(id, detalle));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontró el detalle", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un detalle", description = "Borra un detalle utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDetalle(@PathVariable Integer id) {
        String resultado = detalleService.eliminarDetalle(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}