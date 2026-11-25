package com.mineStore.MineMine.Model.Usuarioss;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 

@Table (name = "comunas")
public class Comuna {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id_comuna;

    @Column (name = "nombre_comuna", nullable = false)
    @Size (min = 2, max = 50, message = "El nombre de la comuna debe tener entre 2 y 50 caracteres")
    private String nombreComuna;

    @ManyToOne
    @JoinColumn (name = "region_id", nullable = false)
    private Region region;

    @OneToMany (mappedBy = "comuna")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Usuario> usuarios;

}