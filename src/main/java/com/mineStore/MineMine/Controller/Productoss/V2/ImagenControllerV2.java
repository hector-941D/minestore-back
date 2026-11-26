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

import com.mineStore.MineMine.Assemblers.Productoss.ImagenModelAssembler;
import com.mineStore.MineMine.DTO.Productoss.ImagenDTO;
import com.mineStore.MineMine.Model.Productoss.Imagen;
import com.mineStore.MineMine.Service.Productoss.ImagenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Imagenes V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Imagenes")
@RestController
@RequestMapping("/api/v2/imagenes")
public class ImagenControllerV2 {

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private ImagenModelAssembler assembler;

    @Operation(summary = "Obtener todas las imagenes (HATEOAS)", description = "Devuelve la lista completa de imagenes con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ImagenDTO>>> obtenerTodasImagenes() {
        List<EntityModel<ImagenDTO>> imagenes = imagenService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                imagenes,
                linkTo(methodOn(ImagenControllerV2.class).obtenerTodasImagenes()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar imagen por ID (HATEOAS)", description = "Devuelve una imagen segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ImagenDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            ImagenDTO imagen = imagenService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(imagen));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar una imagen (HATEOAS)", description = "Registra una imagen")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ImagenDTO>> guardarImagen(@Valid @RequestBody Imagen imagenes) {
        try {
            ImagenDTO guardada = imagenService.guardarImagen(imagenes);
            return ResponseEntity
                    .created(linkTo(methodOn(ImagenControllerV2.class).buscarPorId(guardada.getId_imagen())).toUri())
                    .body(assembler.toModel(guardada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una imagen (HATEOAS)", description = "Actualiza buscando por ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ImagenDTO>> actualizarImagen(@PathVariable Integer id, @Valid @RequestBody Imagen imagen) {
        try {
            ImagenDTO actualizada = imagenService.actualizarImagen(id, imagen);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar una imagen", description = "Borra una imagen utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarImagen(@PathVariable Integer id) {
        String resultado = imagenService.eliminarImagen(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}