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

import com.mineStore.MineMine.Assemblers.Ventass.MetodoPagoModelAssembler;
import com.mineStore.MineMine.DTO.Ventass.MetodoPagoDTO;
import com.mineStore.MineMine.Model.Ventass.MetodoPago;
import com.mineStore.MineMine.Service.Ventass.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Metodos de Pago V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Metodos de Pago")
@RestController
@RequestMapping("/api/v2/metodos-pago")
public class MetodoPagoControllerV2 {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Autowired
    private MetodoPagoModelAssembler assembler;

    @Operation(summary = "Obtener todos los metodos de pago (HATEOAS)", description = "Devuelve la lista completa de metodos de pago con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<MetodoPagoDTO>>> obtenerTodosMetodosPago() {
        List<EntityModel<MetodoPagoDTO>> metodos = metodoPagoService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (metodos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                metodos,
                linkTo(methodOn(MetodoPagoControllerV2.class).obtenerTodosMetodosPago()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar metodo de pago por ID (HATEOAS)", description = "Devuelve un metodo de pago segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            MetodoPagoDTO metodoPago = metodoPagoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(metodoPago));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar un metodo de pago (HATEOAS)", description = "Registra un nuevo metodo de pago y devuelve su representacion HATEOAS")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoDTO>> guardarMetodoPago(@Valid @RequestBody MetodoPago metodoPago) {
        try {
            MetodoPagoDTO guardado = metodoPagoService.guardarMetodoPago(metodoPago);
            return ResponseEntity
                    .created(linkTo(methodOn(MetodoPagoControllerV2.class).buscarPorId(guardado.getId_metodo_pago())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un metodo de pago (HATEOAS)", description = "Actualiza un metodo de pago buscandolo por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MetodoPagoDTO>> actualizarMetodoPago(@PathVariable Integer id, @Valid @RequestBody MetodoPago metodoPago) {
        try {
            MetodoPagoDTO actualizado = metodoPagoService.actualizarMetodoPago(id, metodoPago);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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