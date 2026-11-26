package com.mineStore.MineMine.Assemblers.Productoss;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Productoss.V2.CategoriaControllerV2;
import com.mineStore.MineMine.DTO.Productoss.CategoriaDTO;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaDTO, EntityModel<CategoriaDTO>> {

    @Override
    public EntityModel<CategoriaDTO> toModel(CategoriaDTO categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaControllerV2.class).buscarPorId(categoria.getId_categoria())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).obtenerTodasCategorias()).withRel("categorias"));
    }
}