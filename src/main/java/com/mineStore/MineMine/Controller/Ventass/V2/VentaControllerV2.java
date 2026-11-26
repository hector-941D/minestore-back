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

import com.mineStore.MineMine.Assemblers.Ventass.VentaModelAssembler;
import com.mineStore.MineMine.DTO.Ventass.VentaDTO;
import com.mineStore.MineMine.Model.Ventass.Venta;
import com.mineStore.MineMine.Service.Ventass.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Ventas V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Ventas")
@RestController
@RequestMapping("/api/v2/ventas")
public class VentaControllerV2 {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaModelAssembler assembler;

    @Operation(summary = "Obtener todas las ventas (HATEOAS)", description = "Devuelve la lista completa de ventas con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<VentaDTO>>> obtenerTodasVentas() {
        List<EntityModel<VentaDTO>> ventas = ventaService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                ventas,
                linkTo(methodOn(VentaControllerV2.class).obtenerTodasVentas()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar venta por ID (HATEOAS)", description = "Devuelve una venta segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<VentaDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            VentaDTO venta = ventaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(venta));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar una venta (HATEOAS)", description = "Registra una nueva venta junto con sus detalles y devuelve su representacion HATEOAS")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<VentaDTO>> guardarVenta(@Valid @RequestBody Venta venta) {
        try {
            VentaDTO guardada = ventaService.guardarVenta(venta);
            return ResponseEntity
                    .created(linkTo(methodOn(VentaControllerV2.class).buscarPorId(guardada.getId_venta())).toUri())
                    .body(assembler.toModel(guardada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una venta (HATEOAS)", description = "Actualiza una venta buscandola por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<VentaDTO>> actualizarVenta(@PathVariable Integer id, @Valid @RequestBody Venta venta) {
        try {
            VentaDTO actualizada = ventaService.actualizarVenta(id, venta);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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