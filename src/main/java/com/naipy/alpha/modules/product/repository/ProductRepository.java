package com.naipy.alpha.modules.product.repository;

import com.naipy.alpha.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByOwnerId (String id);

    List<Product> findAllByNameContainingIgnoreCase (String name);

    @Query("SELECT p FROM Product p JOIN p.owner u JOIN u.usersAddresses uaddress ON uaddress.usageType = 'BUSINESS' WHERE LOWER(p.name)" +
            " LIKE %:searching%" +
            " AND uaddress.longitude <= :lngMaior AND uaddress.longitude >= :lngMenor" +
            " AND uaddress.latitude <= :latMaior AND uaddress.latitude >= :latMenor")
    List<Product> findAllByLngLat (@Param("searching") String searchingFor,
                                   @Param("lngMaior") Double lngMaior,
                                   @Param("lngMenor") Double lngMenor,
                                   @Param("latMaior") Double latMaior,
                                   @Param("latMenor") Double latMenor);
}
