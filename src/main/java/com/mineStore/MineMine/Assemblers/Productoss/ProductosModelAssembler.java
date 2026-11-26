package com.mineStore.MineMine.Assemblers.Productoss;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mineStore.MineMine.Controller.Productoss.V2.ProductosControllerV2;
import com.mineStore.MineMine.DTO.Productoss.ProductosDTO;

@Component
public class ProductosModelAssembler implements RepresentationModelAssembler<ProductosDTO, EntityModel<ProductosDTO>> {

    @Override
    public EntityModel<ProductosDTO> toModel(ProductosDTO producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductosControllerV2.class).buscarPorId(producto.getId_producto())).withSelfRel(),
                linkTo(methodOn(ProductosControllerV2.class).obtenerTodosProductos()).withRel("productos"));
    }
}