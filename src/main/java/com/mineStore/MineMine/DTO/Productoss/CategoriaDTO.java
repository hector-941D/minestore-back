package com.mineStore.MineMine.DTO.Productoss;

import java.util.List;

import lombok.Data;

@Data 
public class CategoriaDTO {

    private Integer id_categoria;
    private String nombreCategoria;
    private List<String> nombreProductos;
}