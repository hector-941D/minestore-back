package com.mineStore.MineMine.DTO;

import lombok.Data;

@Data
public class DetalleDTO {

    private Integer id_detalle;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private Integer id_venta;
    private Integer id_producto;
    private String nombreProducto;
}