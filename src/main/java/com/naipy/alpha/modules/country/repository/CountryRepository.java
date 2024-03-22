package com.naipy.alpha.modules.country.repository;

import com.naipy.alpha.modules.country.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {

    boolean existsByName (String name);
}
