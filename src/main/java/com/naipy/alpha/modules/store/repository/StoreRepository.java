package com.naipy.alpha.modules.store.repository;

import com.naipy.alpha.modules.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByOwnerId (Long id);

    void deleteByOwnerId (Long id);
}
