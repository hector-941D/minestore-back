package com.mineStore.MineMine.Controller.Ventass.V2;

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

import com.mineStore.MineMine.Assemblers.Ventass.TipoModelAssembler;
import com.mineStore.MineMine.DTO.Ventass.TipoDTO;
import com.mineStore.MineMine.Model.Ventass.Tipo;
import com.mineStore.MineMine.Service.Ventass.TipoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Tipos V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Tipos de Documento")
@RestController
@RequestMapping("/api/v2/tipos")
public class TipoControllerV2 {

    @Autowired
    private TipoService tipoService;

    @Autowired
    private TipoModelAssembler assembler;

    @Operation(summary = "Obtener todos los tipos (HATEOAS)", description = "Devuelve la lista completa de tipos con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<TipoDTO>>> obtenerTodosTipos() {
        List<EntityModel<TipoDTO>> tipos = tipoService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (tipos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                tipos,
                linkTo(methodOn(TipoControllerV2.class).obtenerTodosTipos()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar tipo por ID (HATEOAS)", description = "Devuelve un tipo segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            TipoDTO tipo = tipoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(tipo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar un tipo (HATEOAS)", description = "Registra un nuevo tipo y devuelve su representacion HATEOAS")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoDTO>> guardarTipo(@Valid @RequestBody Tipo tipo) {
        try {
            TipoDTO guardado = tipoService.guardarTipo(tipo);
            return ResponseEntity
                    .created(linkTo(methodOn(TipoControllerV2.class).buscarPorId(guardado.getId_tipo())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un tipo (HATEOAS)", description = "Actualiza un tipo buscandolo por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoDTO>> actualizarTipo(@PathVariable Integer id, @Valid @RequestBody Tipo tipo) {
        try {
            TipoDTO actualizado = tipoService.actualizarTipo(id, tipo);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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