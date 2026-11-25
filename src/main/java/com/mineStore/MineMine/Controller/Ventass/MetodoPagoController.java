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

import com.mineStore.MineMine.DTO.Ventass.MetodoPagoDTO;
import com.mineStore.MineMine.Model.Ventass.MetodoPago;
import com.mineStore.MineMine.Service.Ventass.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Métodos de Pago", description = "Operaciones CRUD para Métodos de Pago")
@RestController
@RequestMapping("/api/v1/metodos-pago")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Operation(summary = "Obtener todos los metodos de pago", description = "Devuelve la lista completa de metodos de pago registrados")
    @GetMapping
    public ResponseEntity<List<MetodoPagoDTO>> obtenerTodosMetodosPago() {
        return ResponseEntity.ok(metodoPagoService.obtenerTodos());
    }

    @Operation(summary = "Buscar metodo de pago por ID", description = "Devuelve un metodo de pago según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(metodoPagoService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontró el metodo de pago", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar un metodo de pago", description = "Registra un nuevo metodo de pago")
    @PostMapping
    public ResponseEntity<?> guardarMetodoPago(@Valid @RequestBody MetodoPago metodoPago) {
        try {
            MetodoPagoDTO guardado = metodoPagoService.guardarMetodoPago(metodoPago);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar el metodo de pago, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un metodo de pago", description = "Actualiza un metodo de pago buscándolo por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMetodoPago(@PathVariable Integer id, @Valid @RequestBody MetodoPago metodoPago) {
        try {
            return ResponseEntity.ok(metodoPagoService.actualizarMetodoPago(id, metodoPago));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontró el método de pago", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un metodo de pago", description = "Borra un metodo de pago utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMetodoPago(@PathVariable Integer id) {
        String resultado = metodoPagoService.eliminarMetodoPago(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}