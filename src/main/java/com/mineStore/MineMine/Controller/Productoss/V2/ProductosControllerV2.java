package com.mineStore.MineMine.Controller.Productoss.V2;

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

import com.mineStore.MineMine.Assemblers.Productoss.ProductosModelAssembler;
import com.mineStore.MineMine.DTO.Productoss.ProductosDTO;
import com.mineStore.MineMine.Model.Productoss.Productos;
import com.mineStore.MineMine.Service.Productoss.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Productos V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Productos")
@RestController
@RequestMapping("/api/v2/productos")
public class ProductosControllerV2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductosModelAssembler assembler;

    @Operation(summary = "Obtener todos los productos (HATEOAS)", description = "Devuelve la lista completa de productos con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ProductosDTO>>> obtenerTodosProductos() {
        List<EntityModel<ProductosDTO>> productos = productoService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                productos,
                linkTo(methodOn(ProductosControllerV2.class).obtenerTodosProductos()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar producto por ID (HATEOAS)", description = "Devuelve un producto segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductosDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            ProductosDTO producto = productoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(producto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar un producto (HATEOAS)", description = "Registra un producto y devuelve su representacion HATEOAS")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductosDTO>> guardarProducto(@Valid @RequestBody Productos producto) {
        try {
            ProductosDTO guardado = productoService.guardarProducto(producto);
            return ResponseEntity
                    .created(linkTo(methodOn(ProductosControllerV2.class).buscarPorId(guardado.getId_producto())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un producto (HATEOAS)", description = "Actualiza un producto segun su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductosDTO>> actualizarProducto(@PathVariable Integer id, @Valid @RequestBody Productos producto) {
        try {
            ProductosDTO actualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un producto", description = "Borra un producto utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer id) {
        String resultado = productoService.eliminarProducto(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>("Error, No se encontro el producto a borrar ", HttpStatus.NOT_FOUND);
    }
}