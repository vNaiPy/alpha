package com.naipy.alpha.modules.user_address.repository;

import com.naipy.alpha.modules.user_address.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

}
