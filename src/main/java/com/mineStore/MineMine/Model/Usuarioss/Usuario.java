package com.mineStore.MineMine.Model.Usuarioss;

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

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 

@Table(name = "usuarios") 
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;

    @Column (name = "nombre_usuario", nullable = false)
    @Size (min = 2, max = 50, message = "El nombre del usuario debe tener entre 2 y 50 caracteres")
    private String nombre_usuario;

    @Column (name = "correo_electronico", nullable = false, unique = true)
    @Size (min = 5, max = 50, message = "El correo electronico del usuario debe tener entre 5 y 50 caracteres")
    private String correo_electronico;

    @Column (name = "contrasena", nullable = false)
    @Size (min = 8, max = 300, message = "La contraseña del usuario debe tener entre 8 y 300 caracteres")
    private String contrasena;

    @ManyToOne 
    @JoinColumn (name = "rol_id", nullable = false)
    private Rol rol;

    @ManyToOne 
    @JoinColumn (name = "comuna_id", nullable = false)
    private Comuna comuna;
}