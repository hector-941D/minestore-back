package com.mineStore.MineMine.DTO.Usuarioss;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Integer id_usuario;
    private String nombre_usuario;
    private String correo_electronico;
    private Integer id_rol;
    private String nombreRol;
    private Integer id_comuna;
    private String nombreComuna;
}