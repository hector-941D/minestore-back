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

import com.mineStore.MineMine.DTO.Usuarioss.RegionDTO;
import com.mineStore.MineMine.Model.Usuarioss.Region;
import com.mineStore.MineMine.Service.Usuarioss.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Regiones", description = "Operaciones CRUD para Regiones")
@RestController
@RequestMapping("/api/v1/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Operation(summary = "Obtener todas las regiones", description = "Devuelve la lista completa de regiones registradas")
    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerTodasRegiones() {
        return ResponseEntity.ok(regionService.obtenerTodos());
    }

    @Operation(summary = "Buscar región por ID", description = "Devuelve una región según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(regionService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontró la región", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar una región", description = "Registra una nueva región")
    @PostMapping
    public ResponseEntity<?> guardarRegion(@Valid @RequestBody Region region) {
        try {
            RegionDTO guardada = regionService.guardarRegion(region);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar la región, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar una región", description = "Actualiza una región buscándola por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRegion(@PathVariable Integer id, @Valid @RequestBody Region region) {
        try {
            return ResponseEntity.ok(regionService.actualizarRegion(id, region));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontró la región", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar una región", description = "Borra una región utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRegion(@PathVariable Integer id) {
        String resultado = regionService.eliminarRegion(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}