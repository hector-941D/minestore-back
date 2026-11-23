package com.mineStore.MineMine.DTO.Productoss;

import java.util.List;

import lombok.Data;

@Data 
public class ImagenDTO {

    private Integer id_imagen;
    private String urlImagen;
    private List<String> nombreProductos;
}