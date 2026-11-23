package com.mineStore.MineMine.Repository.Productoss;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mineStore.MineMine.Model.Productoss.Imagen;

@Repository 
public interface ImagenRepository extends JpaRepository<Imagen, Integer> {

}
