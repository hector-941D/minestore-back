package com.mineStore.MineMine.DTO.Usuarioss;

import java.util.List;

import lombok.Data;

@Data
public class ComunaDTO {

    private Integer id_comuna;
    private String nombreComuna;
    private Integer id_region;
    private String nombreRegion;
    private List<String> nombreUsuarios;
}