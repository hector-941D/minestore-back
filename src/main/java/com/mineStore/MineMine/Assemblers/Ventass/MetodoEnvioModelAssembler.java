package com.mineStore.MineMine.Assemblers.Ventass;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Ventass.V2.MetodoEnvioControllerV2;
import com.mineStore.MineMine.DTO.Ventass.MetodoEnvioDTO;

@Component
public class MetodoEnvioModelAssembler implements RepresentationModelAssembler<MetodoEnvioDTO, EntityModel<MetodoEnvioDTO>> {

    @Override
    public EntityModel<MetodoEnvioDTO> toModel(MetodoEnvioDTO metodoEnvio) {
        return EntityModel.of(metodoEnvio,
                linkTo(methodOn(MetodoEnvioControllerV2.class).buscarPorId(metodoEnvio.getId_metodo_envio())).withSelfRel(),
                linkTo(methodOn(MetodoEnvioControllerV2.class).obtenerTodosMetodosEnvio()).withRel("metodos-envio"));
    }
}