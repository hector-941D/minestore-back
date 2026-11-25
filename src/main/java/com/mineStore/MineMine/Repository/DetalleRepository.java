package com.mineStore.MineMine.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mineStore.MineMine.Model.Detalle;

@Repository 
public interface DetalleRepository extends JpaRepository<Detalle, Integer> {

}
