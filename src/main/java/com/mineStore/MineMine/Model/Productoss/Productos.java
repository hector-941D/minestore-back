package com.mineStore.MineMine.Model.Productoss;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor 
@AllArgsConstructor 
@Data 
@Entity

@Table(name = "productos") 
public class Productos {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;

    @Column(name = "nombre_producto", nullable = false)
    @Size(min = 2, max = 50, message = "El nombre del producto debe tener entre 2 y 50 caracteres")
    private String nombreProducto;

    @Column (name = "descripcion", nullable = false)
    @Size(min = 5, max = 200, message = "La descripción del producto debe tener entre 5 y 200 caracteres")
    private String descripcionProducto;

    @Column (name = "precio", nullable = false)
    private Double precioProducto;

    @Column (name = "stock", nullable = false)
    private Integer stockProducto;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne 
    @JoinColumn (name = "imagen_id", nullable = false)
    private Imagen imagen;
}