package com.mineStore.MineMine.Assemblers.Ventass;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Ventass.V2.TipoControllerV2;
import com.mineStore.MineMine.DTO.Ventass.TipoDTO;

@Component
public class TipoModelAssembler implements RepresentationModelAssembler<TipoDTO, EntityModel<TipoDTO>> {

    @Override
    public EntityModel<TipoDTO> toModel(TipoDTO tipo) {
        return EntityModel.of(tipo,
                linkTo(methodOn(TipoControllerV2.class).buscarPorId(tipo.getId_tipo())).withSelfRel(),
                linkTo(methodOn(TipoControllerV2.class).obtenerTodosTipos()).withRel("tipos"));
    }
}