package com.naipy.alpha.modules.order_item.repository;

import com.naipy.alpha.modules.order_item.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
