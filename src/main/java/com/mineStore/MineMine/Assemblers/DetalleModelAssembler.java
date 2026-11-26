package com.mineStore.MineMine.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.DetalleControllerV2;
import com.mineStore.MineMine.DTO.DetalleDTO;

@Component
public class DetalleModelAssembler implements RepresentationModelAssembler<DetalleDTO, EntityModel<DetalleDTO>> {

    @Override
    public EntityModel<DetalleDTO> toModel(DetalleDTO detalle) {
        return EntityModel.of(detalle,
                linkTo(methodOn(DetalleControllerV2.class).buscarPorId(detalle.getId_detalle())).withSelfRel(),
                linkTo(methodOn(DetalleControllerV2.class).obtenerTodosDetalles()).withRel("detalles"));
    }
}