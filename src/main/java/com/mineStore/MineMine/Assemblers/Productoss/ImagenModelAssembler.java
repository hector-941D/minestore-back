package com.mineStore.MineMine.Assemblers.Productoss;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Productoss.V2.ImagenControllerV2;
import com.mineStore.MineMine.DTO.Productoss.ImagenDTO;

@Component
public class ImagenModelAssembler implements RepresentationModelAssembler<ImagenDTO, EntityModel<ImagenDTO>> {

    @Override
    public EntityModel<ImagenDTO> toModel(ImagenDTO imagen) {
        return EntityModel.of(imagen,
                linkTo(methodOn(ImagenControllerV2.class).buscarPorId(imagen.getId_imagen())).withSelfRel(),
                linkTo(methodOn(ImagenControllerV2.class).obtenerTodasImagenes()).withRel("imagenes"));
    }
}