package com.naipy.alpha.repositories;

import com.naipy.alpha.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByOwnerId (Long id);

    void deleteByOwnerId (Long id);
}
