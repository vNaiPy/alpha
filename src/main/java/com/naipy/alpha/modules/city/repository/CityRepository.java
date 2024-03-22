package com.naipy.alpha.modules.city.repository;

import com.naipy.alpha.modules.city.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
    boolean existsByName (String name);
}
