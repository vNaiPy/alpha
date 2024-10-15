package com.naipy.alpha.modules.user_address.repository;

import com.naipy.alpha.modules.user_address.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, String> {

    @Query(value = "SELECT * from tb_user_address ua WHERE ua.user_id = ?1", nativeQuery = true)
    Set<UserAddress> findUserAddressByUserId (String userId);
}
