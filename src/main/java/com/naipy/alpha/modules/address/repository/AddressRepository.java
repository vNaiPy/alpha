package com.naipy.alpha.modules.address.repository;

import com.naipy.alpha.modules.address.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository  extends JpaRepository<Address, String> {

    @Query("SELECT adss FROM Address adss WHERE adss.zipcode = :zipcode")
    Optional<Address> findAddressByZipCode (@Param("zipcode") String zipcode);
}
