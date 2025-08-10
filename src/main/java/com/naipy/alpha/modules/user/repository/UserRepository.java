package com.naipy.alpha.modules.user.repository;

import com.naipy.alpha.modules.user.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail (String email);
    Optional<User> findByUsername (String username);
    List<User> findByUsernameContainingIgnoreCase (String username);

}
