package com.mineStore.MineMine.Model.Ventass;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "metodos_pago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_metodo_pago;

    @Column(name = "nombre_metodo_pago", nullable = false, unique = true)
    private String nombreMetodoPago;

    @OneToMany(mappedBy = "metodoPago")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Venta> ventas;
}