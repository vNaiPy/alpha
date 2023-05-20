package com.naipy.alpha.services;

import com.naipy.alpha.entities.Order;
import com.naipy.alpha.entities.dto.OrderDTO;
import com.naipy.alpha.repositories.OrderRepository;
import com.naipy.alpha.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository _orderRepository;

    public List<OrderDTO> findAll () {
        return _orderRepository.findAll().stream().map(OrderDTO::createOrderDTO).collect(Collectors.toList());
    }

    public Order findById (Long id) {
        Optional<Order> orderOptional = _orderRepository.findById(id);
        if(orderOptional.isEmpty()) throw new ResourceNotFoundException("Order not found. Id: " + id);
        return orderOptional.get();
    }
}
