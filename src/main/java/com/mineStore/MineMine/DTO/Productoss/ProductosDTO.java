package com.mineStore.MineMine.DTO.Productoss;

import lombok.Data;

@Data 
public class ProductosDTO {

    private Integer id_producto;
    private String nombreProducto;
    private String descripcionProducto;
    private Double precioProducto;
    private Integer stockProducto;
    private Integer id_categoria;
    private Integer id_imagen;
}