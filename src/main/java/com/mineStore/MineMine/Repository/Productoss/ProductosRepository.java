package com.mineStore.MineMine.Repository.Productoss;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mineStore.MineMine.Model.Productoss.Productos;

@Repository 
public interface ProductosRepository extends JpaRepository<Productos, Integer> {
    
}