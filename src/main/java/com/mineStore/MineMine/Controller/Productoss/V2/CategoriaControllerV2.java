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

import com.mineStore.MineMine.Assemblers.Productoss.CategoriaModelAssembler;
import com.mineStore.MineMine.DTO.Productoss.CategoriaDTO;
import com.mineStore.MineMine.Model.Productoss.Categoria;
import com.mineStore.MineMine.Service.Productoss.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Categorias V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Categorias")
@RestController
@RequestMapping("/api/v2/categorias")
public class CategoriaControllerV2 {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaModelAssembler assembler;

    @Operation(summary = "Obtener todas las categorias (HATEOAS)", description = "Devuelve la lista completa de categorias con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> obtenerTodasCategorias() {
        List<EntityModel<CategoriaDTO>> categorias = categoriaService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                categorias,
                linkTo(methodOn(CategoriaControllerV2.class).obtenerTodasCategorias()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar categoria por ID (HATEOAS)", description = "Devuelve una categoria segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CategoriaDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            CategoriaDTO categoria = categoriaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(categoria));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar una categoria (HATEOAS)", description = "Registra una nueva categoria")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CategoriaDTO>> guardarCategoria(@Valid @RequestBody Categoria categoria) {
        try {
            CategoriaDTO guardada = categoriaService.guardarCategoria(categoria);
            return ResponseEntity
                    .created(linkTo(methodOn(CategoriaControllerV2.class).buscarPorId(guardada.getId_categoria())).toUri())
                    .body(assembler.toModel(guardada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una categoria (HATEOAS)", description = "Actualiza una categoria buscandola por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CategoriaDTO>> actualizarCategoria(@PathVariable Integer id, @Valid @RequestBody Categoria categoria) {
        try {
            CategoriaDTO actualizada = categoriaService.actualizarCategoria(id, categoria);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar una categoria", description = "Borra una categoria utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Integer id) {
        String resultado = categoriaService.eliminarCategoria(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}