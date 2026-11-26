package com.mineStore.MineMine.Controller.Usuarioss.V1;

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

import com.mineStore.MineMine.DTO.Usuarioss.ComunaDTO;
import com.mineStore.MineMine.Model.Usuarioss.Comuna;
import com.mineStore.MineMine.Service.Usuarioss.ComunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Comunas", description = "Operaciones CRUD para Comunas")
@RestController
@RequestMapping("/api/v1/comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @Operation(summary = "Obtener todas las comunas", description = "Devuelve la lista completa de comunas registradas")
    @GetMapping
    public ResponseEntity<List<ComunaDTO>> obtenerTodasComunas() {
        return ResponseEntity.ok(comunaService.obtenerTodos());
    }

    @Operation(summary = "Buscar comuna por ID", description = "Devuelve una comuna segun su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(comunaService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontro la comuna", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar una comuna", description = "Registra una nueva comuna")
    @PostMapping
    public ResponseEntity<?> guardarComuna(@Valid @RequestBody Comuna comuna) {
        try {
            ComunaDTO guardada = comunaService.guardarComuna(comuna);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar la comuna, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar una comuna", description = "Actualiza una comuna buscandola por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarComuna(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        try {
            return ResponseEntity.ok(comunaService.actualizarComuna(id, comuna));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontro la comuna", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar una comuna", description = "Borra una comuna utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarComuna(@PathVariable Integer id) {
        String resultado = comunaService.eliminarComuna(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}