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

import com.mineStore.MineMine.Assemblers.Ventass.MetodoEnvioModelAssembler;
import com.mineStore.MineMine.DTO.Ventass.MetodoEnvioDTO;
import com.mineStore.MineMine.Model.Ventass.MetodoEnvio;
import com.mineStore.MineMine.Service.Ventass.MetodoEnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Metodos de Envio V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Metodos de Envio")
@RestController
@RequestMapping("/api/v2/metodos-envio")
public class MetodoEnvioControllerV2 {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @Autowired
    private MetodoEnvioModelAssembler assembler;

    @Operation(summary = "Obtener todos los metodos de envio (HATEOAS)", description = "Devuelve la lista completa de metodos de envio con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<MetodoEnvioDTO>>> obtenerTodosMetodosEnvio() {
        List<EntityModel<MetodoEnvioDTO>> metodos = metodoEnvioService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (metodos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                metodos,
                linkTo(methodOn(MetodoEnvioControllerV2.class).obtenerTodosMetodosEnvio()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar metodo de envio por ID (HATEOAS)", description = "Devuelve un metodo de envio segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoEnvioDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            MetodoEnvioDTO metodoEnvio = metodoEnvioService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(metodoEnvio));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar un metodo de envio (HATEOAS)", description = "Registra un nuevo metodo de envio y devuelve su representacion HATEOAS")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoEnvioDTO>> guardarMetodoEnvio(@Valid @RequestBody MetodoEnvio metodoEnvio) {
        try {
            MetodoEnvioDTO guardado = metodoEnvioService.guardarMetodoEnvio(metodoEnvio);
            return ResponseEntity
                    .created(linkTo(methodOn(MetodoEnvioControllerV2.class).buscarPorId(guardado.getId_metodo_envio())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un metodo de envio (HATEOAS)", description = "Actualiza un metodo de envio buscandolo por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoEnvioDTO>> actualizarMetodoEnvio(@PathVariable Integer id, @Valid @RequestBody MetodoEnvio metodoEnvio) {
        try {
            MetodoEnvioDTO actualizado = metodoEnvioService.actualizarMetodoEnvio(id, metodoEnvio);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un metodo de envio", description = "Borra un metodo de envio utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMetodoEnvio(@PathVariable Integer id) {
        String resultado = metodoEnvioService.eliminarMetodoEnvio(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}