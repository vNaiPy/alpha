package com.naipy.alpha.modules.store.repository;

import com.naipy.alpha.modules.store.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByOwnerId (UUID id);

    void deleteByOwnerId (UUID id);
}
