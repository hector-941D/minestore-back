package com.mineStore.MineMine.DTO.Ventass;

import java.time.LocalDateTime;
import java.util.List;

import com.mineStore.MineMine.DTO.DetalleDTO;

import lombok.Data;

@Data
public class VentaDTO {

    private Integer id_venta;
    private LocalDateTime fechaVenta;
    private Double totalVenta;
    private Integer id_usuario;
    private String nombreUsuario;
    private Integer id_tipo;
    private String nombreTipo;
    private Integer id_metodo_pago;
    private String nombreMetodoPago;
    private Integer id_metodo_envio;
    private String nombreMetodoEnvio;
    private List<DetalleDTO> detalles;
}