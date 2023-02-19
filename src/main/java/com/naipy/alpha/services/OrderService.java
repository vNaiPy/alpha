package com.naipy.alpha.services;

import com.naipy.alpha.entities.Order;
import com.naipy.alpha.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository _orderRepository;

    public List<Order> findAll () {
        return _orderRepository.findAll();
    }

    public Order findById (Long id) {
        Optional<Order> orderOptional = _orderRepository.findById(id);
        return orderOptional.get();
    }
}
