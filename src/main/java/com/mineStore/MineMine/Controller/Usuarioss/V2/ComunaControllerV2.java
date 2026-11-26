package com.mineStore.MineMine.Controller.Usuarioss.V2;

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

import com.mineStore.MineMine.Assemblers.Usuarioss.ComunaModelAssembler;
import com.mineStore.MineMine.DTO.Usuarioss.ComunaDTO;
import com.mineStore.MineMine.Model.Usuarioss.Comuna;
import com.mineStore.MineMine.Service.Usuarioss.ComunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Comunas V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Comunas")
@RestController
@RequestMapping("/api/v2/comunas")
public class ComunaControllerV2 {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaModelAssembler assembler;

    @Operation(summary = "Obtener todas las comunas (HATEOAS)", description = "Devuelve la lista completa de comunas con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ComunaDTO>>> obtenerTodasComunas() {
        List<EntityModel<ComunaDTO>> comunas = comunaService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (comunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                comunas,
                linkTo(methodOn(ComunaControllerV2.class).obtenerTodasComunas()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar comuna por ID (HATEOAS)", description = "Devuelve una comuna segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            ComunaDTO comuna = comunaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(comuna));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar una comuna (HATEOAS)", description = "Registra una nueva comuna y devuelve su representacion HATEOAS")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> guardarComuna(@Valid @RequestBody Comuna comuna) {
        try {
            ComunaDTO guardada = comunaService.guardarComuna(comuna);
            return ResponseEntity
                    .created(linkTo(methodOn(ComunaControllerV2.class).buscarPorId(guardada.getId_comuna())).toUri())
                    .body(assembler.toModel(guardada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una comuna (HATEOAS)", description = "Actualiza una comuna buscandola por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> actualizarComuna(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        try {
            ComunaDTO actualizada = comunaService.actualizarComuna(id, comuna);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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