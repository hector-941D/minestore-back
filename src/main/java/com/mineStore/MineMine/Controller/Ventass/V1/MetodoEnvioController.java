package com.mineStore.MineMine.Controller.Ventass.V1;

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

import com.mineStore.MineMine.DTO.Ventass.MetodoEnvioDTO;
import com.mineStore.MineMine.Model.Ventass.MetodoEnvio;
import com.mineStore.MineMine.Service.Ventass.MetodoEnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Métodos de Envío", description = "Operaciones CRUD para Métodos de Envío")
@RestController
@RequestMapping("/api/v1/metodos-envio")
public class MetodoEnvioController {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @Operation(summary = "Obtener todos los metodos de envío", description = "Devuelve la lista completa de métodos de envío registrados")
    @GetMapping
    public ResponseEntity<List<MetodoEnvioDTO>> obtenerTodosMetodosEnvio() {
        return ResponseEntity.ok(metodoEnvioService.obtenerTodos());
    }

    @Operation(summary = "Buscar metodo de envio por ID", description = "Devuelve un metodo de envío según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(metodoEnvioService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontro el método de envío", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar un metodo de envío", description = "Registra un nuevo metodo de envío")
    @PostMapping
    public ResponseEntity<?> guardarMetodoEnvio(@Valid @RequestBody MetodoEnvio metodoEnvio) {
        try {
            MetodoEnvioDTO guardado = metodoEnvioService.guardarMetodoEnvio(metodoEnvio);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar el metodo de envío, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un metodo de envío", description = "Actualiza un metodo de envío buscandolo por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMetodoEnvio(@PathVariable Integer id, @Valid @RequestBody MetodoEnvio metodoEnvio) {
        try {
            return ResponseEntity.ok(metodoEnvioService.actualizarMetodoEnvio(id, metodoEnvio));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontró el método de envío", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un metodo de envío", description = "Borra un metodo de envío utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMetodoEnvio(@PathVariable Integer id) {
        String resultado = metodoEnvioService.eliminarMetodoEnvio(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}