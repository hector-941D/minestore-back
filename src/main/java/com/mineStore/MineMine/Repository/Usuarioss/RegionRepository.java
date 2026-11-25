package com.mineStore.MineMine.Repository.Usuarioss;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mineStore.MineMine.Model.Usuarioss.Region;

@Repository 
public interface RegionRepository extends JpaRepository<Region, Integer> {

}
