package com.naipy.alpha.modules.order.service;

import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.order.models.OrderDTO;
import com.naipy.alpha.modules.order.repository.OrderRepository;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
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

    public OrderDTO findById (Long id) {
        Optional<Order> orderOptional = _orderRepository.findById(id);
        if(orderOptional.isEmpty()) throw new ResourceNotFoundException("Order not found. Id: " + id);
        return OrderDTO.createOrderDTO(orderOptional.get());
    }
}
