package com.naipy.alpha.modules.order.repository;

import com.naipy.alpha.modules.order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByUserId (String id);
}
