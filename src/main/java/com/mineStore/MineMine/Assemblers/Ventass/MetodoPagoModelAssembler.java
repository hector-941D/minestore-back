package com.mineStore.MineMine.Assemblers.Ventass;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Ventass.V2.MetodoPagoControllerV2;
import com.mineStore.MineMine.DTO.Ventass.MetodoPagoDTO;

@Component
public class MetodoPagoModelAssembler implements RepresentationModelAssembler<MetodoPagoDTO, EntityModel<MetodoPagoDTO>> {

    @Override
    public EntityModel<MetodoPagoDTO> toModel(MetodoPagoDTO metodoPago) {
        return EntityModel.of(metodoPago,
                linkTo(methodOn(MetodoPagoControllerV2.class).buscarPorId(metodoPago.getId_metodo_pago())).withSelfRel(),
                linkTo(methodOn(MetodoPagoControllerV2.class).obtenerTodosMetodosPago()).withRel("metodos-pago"));
    }
}