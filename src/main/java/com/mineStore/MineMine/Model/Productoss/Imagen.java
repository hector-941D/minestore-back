package com.mineStore.MineMine.Model.Productoss;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor 
@AllArgsConstructor 
@Data 

@Entity 
@Table (name = "imagenes")
public class Imagen {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id_imagen;

    @Column (name = "url_imagen")
    private String urlImagen;

    @OneToMany(mappedBy = "imagen")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Productos> productos;
}