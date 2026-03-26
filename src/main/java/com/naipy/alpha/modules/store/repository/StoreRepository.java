package com.naipy.alpha.modules.store.repository;

import com.naipy.alpha.modules.store.models.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

    Optional<Store> findByOwnerId (String id);
    Optional<Store> findByName (String id);
    Boolean existsByOwnerId (String id);

    @Query("SELECT s FROM Store s WHERE s.name LIKE %?1%")
    Page<Store> findAllByName (String name, Pageable pageable);


    void deleteByOwnerId (String id);
}
