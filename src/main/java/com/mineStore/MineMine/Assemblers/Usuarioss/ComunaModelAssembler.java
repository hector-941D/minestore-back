package com.mineStore.MineMine.Assemblers.Usuarioss;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Usuarioss.V2.ComunaControllerV2;
import com.mineStore.MineMine.DTO.Usuarioss.ComunaDTO;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<ComunaDTO, EntityModel<ComunaDTO>> {

    @Override
    public EntityModel<ComunaDTO> toModel(ComunaDTO comuna) {
        return EntityModel.of(comuna,
                linkTo(methodOn(ComunaControllerV2.class).buscarPorId(comuna.getId_comuna())).withSelfRel(),
                linkTo(methodOn(ComunaControllerV2.class).obtenerTodasComunas()).withRel("comunas"));
    }
}