package com.mineStore.MineMine.Assemblers.Usuarioss;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Usuarioss.V2.RegionControllerV2;
import com.mineStore.MineMine.DTO.Usuarioss.RegionDTO;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<RegionDTO, EntityModel<RegionDTO>> {

    @Override
    public EntityModel<RegionDTO> toModel(RegionDTO region) {
        return EntityModel.of(region,
                linkTo(methodOn(RegionControllerV2.class).buscarPorId(region.getId_region())).withSelfRel(),
                linkTo(methodOn(RegionControllerV2.class).obtenerTodasRegiones()).withRel("regiones"));
    }
}