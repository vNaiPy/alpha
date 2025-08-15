package com.naipy.alpha.modules.order.service;

import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.order.models.OrderDTO;
import com.naipy.alpha.modules.order.repository.OrderRepository;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService extends ServiceUtils {

    private final OrderRepository _orderRepository;

    @Autowired
    public OrderService (OrderRepository orderRepository) {
        this._orderRepository = orderRepository;
    }

    public List<OrderDTO> findAll () {
        return _orderRepository.findAll().stream().map(OrderDTO::createOrderDTO).toList();
    }

    public List<OrderDTO> findAllByCurrentUser () {
        return _orderRepository.findAllByUserId(getIdCurrentUser().getId()).stream().map(OrderDTO::createOrderDTO).toList();
    }

    public OrderDTO findById (String id) {
        Optional<Order> orderOptional = _orderRepository.findById(id);
        if(orderOptional.isEmpty()) throw new ResourceNotFoundException("Order not found. Id: " + id);
        return OrderDTO.createOrderDTO(orderOptional.get());
    }
}
