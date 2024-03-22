package com.naipy.alpha.modules.address.repository;

import com.naipy.alpha.modules.address.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository  extends JpaRepository<Address, UUID> {

    @Query("SELECT addrs FROM Address addrs JOIN addrs.zipCode zp WHERE zp.zipCode = :zipcode")
    Optional<Address> findAddressByZipCode (@Param("zipcode") String zipcode);
}
