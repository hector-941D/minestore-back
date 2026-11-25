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

import com.mineStore.MineMine.DTO.Ventass.VentaDTO;
import com.mineStore.MineMine.Model.Ventass.Venta;
import com.mineStore.MineMine.Service.Ventass.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Ventas", description = "Operaciones CRUD para Ventas")
@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Operation(summary = "Obtener todas las ventas", description = "Devuelve la lista completa de ventas registradas")
    @GetMapping
    public ResponseEntity<List<VentaDTO>> obtenerTodasVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodos());
    }

    @Operation(summary = "Buscar venta por ID", description = "Devuelve una venta según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontro la venta", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar una venta", description = "Registra una nueva venta junto con sus detalles")
    @PostMapping
    public ResponseEntity<?> guardarVenta(@Valid @RequestBody Venta venta) {
        try {
            VentaDTO guardada = ventaService.guardarVenta(venta);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar la venta, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar una venta", description = "Actualiza una venta buscándola por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Integer id, @Valid @RequestBody Venta venta) {
        try {
            return ResponseEntity.ok(ventaService.actualizarVenta(id, venta));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontró la venta", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar una venta", description = "Borra una venta utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVenta(@PathVariable Integer id) {
        String resultado = ventaService.eliminarVenta(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}