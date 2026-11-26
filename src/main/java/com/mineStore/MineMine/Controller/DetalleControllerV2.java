package com.mineStore.MineMine.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
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

import com.mineStore.MineMine.Assemblers.DetalleModelAssembler;
import com.mineStore.MineMine.DTO.DetalleDTO;
import com.mineStore.MineMine.Model.Detalle;
import com.mineStore.MineMine.Service.DetalleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Detalles V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Detalles de Venta")
@RestController
@RequestMapping("/api/v2/detalles")
public class DetalleControllerV2 {

    @Autowired
    private DetalleService detalleService;

    @Autowired
    private DetalleModelAssembler assembler;

    @Operation(summary = "Obtener todos los detalles (HATEOAS)", description = "Devuelve la lista completa de detalles con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<DetalleDTO>>> obtenerTodosDetalles() {
        List<EntityModel<DetalleDTO>> detalles = detalleService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (detalles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                detalles,
                linkTo(methodOn(DetalleControllerV2.class).obtenerTodosDetalles()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar detalle por ID (HATEOAS)", description = "Devuelve un detalle segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetalleDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            DetalleDTO detalle = detalleService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(detalle));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar un detalle (HATEOAS)", description = "Registra un nuevo detalle de venta y devuelve su representacion HATEOAS")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetalleDTO>> guardarDetalle(@Valid @RequestBody Detalle detalle) {
        try {
            DetalleDTO guardado = detalleService.guardarDetalle(detalle);
            return ResponseEntity
                    .created(linkTo(methodOn(DetalleControllerV2.class).buscarPorId(guardado.getId_detalle())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un detalle (HATEOAS)", description = "Actualiza un detalle buscandolo por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetalleDTO>> actualizarDetalle(@PathVariable Integer id, @Valid @RequestBody Detalle detalle) {
        try {
            DetalleDTO actualizado = detalleService.actualizarDetalle(id, detalle);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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