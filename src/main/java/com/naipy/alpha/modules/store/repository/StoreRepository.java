package com.naipy.alpha.modules.store.repository;

import com.naipy.alpha.modules.store.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

    Optional<Store> findByOwnerId (String id);
    Optional<Store> findByName (String id);
    Boolean existsByOwnerId (String id);
    List<Store> findAllByName (String name);


    void deleteByOwnerId (String id);
}
