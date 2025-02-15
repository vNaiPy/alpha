package com.naipy.alpha.modules.product.repository;

import com.naipy.alpha.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByOwnerId (UUID id);

    /*@Query("SELECT p FROM Product p JOIN p.owner u JOIN u.address add WHERE LOWER(p.name) LIKE %:searching% AND add.longitude <= :lngMaior AND add.longitude >= :lngMenor AND add.latitude <= :latMaior AND add.latitude >= :latMenor")
    List<Product> findAllByLngLat (@Param("searching") String searchingFor,
                                   @Param("lngMaior") Double lngMaior,
                                   @Param("lngMenor") Double lngMenor,
                                   @Param("latMaior") Double latMaior,
                                   @Param("latMenor") Double latMenor);*/
}
