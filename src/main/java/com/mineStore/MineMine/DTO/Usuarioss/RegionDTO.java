package com.mineStore.MineMine.DTO.Usuarioss;

import java.util.List;

import lombok.Data;

@Data
public class RegionDTO {

    private Integer id_region;
    private String nombreRegion;
    private List<String> nombreComunas;
}