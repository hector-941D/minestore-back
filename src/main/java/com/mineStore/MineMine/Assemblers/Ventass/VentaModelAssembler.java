package com.mineStore.MineMine.Assemblers.Ventass;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Ventass.V2.VentaControllerV2;
import com.mineStore.MineMine.DTO.Ventass.VentaDTO;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<VentaDTO, EntityModel<VentaDTO>> {

    @Override
    public EntityModel<VentaDTO> toModel(VentaDTO venta) {
        return EntityModel.of(venta,
                linkTo(methodOn(VentaControllerV2.class).buscarPorId(venta.getId_venta())).withSelfRel(),
                linkTo(methodOn(VentaControllerV2.class).obtenerTodasVentas()).withRel("ventas"));
    }
}