package com.mineStore.MineMine.Assemblers.Usuarioss;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Usuarioss.V2.RolControllerV2;
import com.mineStore.MineMine.DTO.Usuarioss.RolDTO;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<RolDTO, EntityModel<RolDTO>> {

    @Override
    public EntityModel<RolDTO> toModel(RolDTO rol) {
        return EntityModel.of(rol,
                linkTo(methodOn(RolControllerV2.class).buscarPorId(rol.getId_rol())).withSelfRel(),
                linkTo(methodOn(RolControllerV2.class).obtenerTodosRoles()).withRel("roles"));
    }
}