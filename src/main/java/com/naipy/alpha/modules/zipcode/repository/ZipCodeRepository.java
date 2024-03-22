package com.naipy.alpha.modules.zipcode.repository;

import com.naipy.alpha.modules.zipcode.models.ZipCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ZipCodeRepository extends JpaRepository<ZipCode, UUID> {
    boolean existsByZipCode (String zipCode);
}
