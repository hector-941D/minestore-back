package com.mineStore.MineMine.DTO.Usuarioss;

import java.util.List;

import lombok.Data;

@Data
public class RolDTO {

    private Integer id_rol;
    private String nombreRol;
    private List<String> nombreUsuarios;
}