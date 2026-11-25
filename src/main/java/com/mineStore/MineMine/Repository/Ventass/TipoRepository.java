package com.mineStore.MineMine.Repository.Ventass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mineStore.MineMine.Model.Ventass.Tipo;

@Repository 
public interface TipoRepository extends JpaRepository<Tipo, Integer> {

}
