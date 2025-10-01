package com.naipy.alpha.modules.product.repository;

import com.naipy.alpha.modules.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findAllByOwnerId (String id, Pageable pageable);

    Page<Product> findAllByNameContainingIgnoreCase (String name, Pageable pageable);

    // Procura os produtos pelo nome e filtra pela localização do endereço comercial do dono do produto
//    @Query("""
//            SELECT p FROM Product p JOIN p.owner u JOIN u.usersAddresses uaddress ON uaddress.usageType = 'BUSINESS'
//            WHERE LOWER(p.name) LIKE %:searching%
//                AND uaddress.latitude BETWEEN :latMin AND :latMax
//                AND uaddress.longitude BETWEEN :lngMin AND :lngMax
//                AND (
//                    6371 * acos(
//                        cos(radians(:lat)) * cos(radians(uaddress.latitude)) *
//                        cos(radians(uaddress.longitude) - radians(:lng)) +
//                        sin(radians(:lat)) * sin(radians(uaddress.latitude))
//                    )
//                ) < :radius
//            """)
//    Page<Product> findAllWithinRadiusByLngLat (@Param("searching") String searchingFor,
//                                               @Param("lng") double lng,
//                                               @Param("lat") double lat,
//                                               @Param("radius") double radius,
//                                               @Param("lngMax") double lngMax,
//                                               @Param("lngMin") double lngMin,
//                                               @Param("latMax") double latMax,
//                                               @Param("latMin") double latMin,
//                                               Pageable pageable);

    @Query("""
            SELECT p FROM Product p JOIN p.owner u JOIN u.usersAddresses uaddress ON uaddress.usageType = 'BUSINESS'
            WHERE LOWER(p.name) LIKE %:searching%
                AND uaddress.latitude BETWEEN :latMin AND :latMax
                AND uaddress.longitude BETWEEN :lngMin AND :lngMax
                AND (
                    6371 * acos(
                        cos(radians(:lat)) * cos(radians(uaddress.latitude)) *
                        cos(radians(uaddress.longitude) - radians(:lng)) +
                        sin(radians(:lat)) * sin(radians(uaddress.latitude))
                    )
                ) < :radius
            """)
    Page<Product> findAllWithinRadiusByLngLat (@Param("searching") String searchingFor,
                                               @Param("lng") double lng,
                                               @Param("lat") double lat,
                                               @Param("radius") double radius,
                                               @Param("lngMax") double lngMax,
                                               @Param("lngMin") double lngMin,
                                               @Param("latMax") double latMax,
                                               @Param("latMin") double latMin,
                                               Pageable pageable);
    
}
