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

import com.mineStore.MineMine.Assemblers.Usuarioss.RolModelAssembler;
import com.mineStore.MineMine.DTO.Usuarioss.RolDTO;
import com.mineStore.MineMine.Model.Usuarioss.Rol;
import com.mineStore.MineMine.Service.Usuarioss.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Roles V2 (HATEOAS)", description = "Operaciones CRUD con HATEOAS para Roles")
@RestController
@RequestMapping("/api/v2/roles")
public class RolControllerV2 {

    @Autowired
    private RolService rolService;

    @Autowired
    private RolModelAssembler assembler;

    @Operation(summary = "Obtener todos los roles (HATEOAS)", description = "Devuelve la lista completa de roles con enlaces HATEOAS")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<RolDTO>>> obtenerTodosRoles() {
        List<EntityModel<RolDTO>> roles = rolService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                roles,
                linkTo(methodOn(RolControllerV2.class).obtenerTodosRoles()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar rol por ID (HATEOAS)", description = "Devuelve un rol segun su ID con enlaces HATEOAS")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RolDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            RolDTO rol = rolService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(rol));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guardar un rol (HATEOAS)", description = "Registra un nuevo rol y devuelve su representacion HATEOAS")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RolDTO>> guardarRol(@Valid @RequestBody Rol rol) {
        try {
            RolDTO guardado = rolService.guardarRol(rol);
            return ResponseEntity
                    .created(linkTo(methodOn(RolControllerV2.class).buscarPorId(guardado.getId_rol())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un rol (HATEOAS)", description = "Actualiza un rol buscandolo por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RolDTO>> actualizarRol(@PathVariable Integer id, @Valid @RequestBody Rol rol) {
        try {
            RolDTO actualizado = rolService.actualizarRol(id, rol);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un rol", description = "Borra un rol utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRol(@PathVariable Integer id) {
        String resultado = rolService.eliminarRol(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}