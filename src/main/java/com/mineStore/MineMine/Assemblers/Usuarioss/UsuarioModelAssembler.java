package com.mineStore.MineMine.Assemblers.Usuarioss;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Usuarioss.V2.UsuarioControllerV2;
import com.mineStore.MineMine.DTO.Usuarioss.UsuarioDTO;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioDTO, EntityModel<UsuarioDTO>> {

    @Override
    public EntityModel<UsuarioDTO> toModel(UsuarioDTO usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioControllerV2.class).buscarPorId(usuario.getId_usuario())).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class).obtenerTodosUsuarios()).withRel("usuarios"));
    }
}