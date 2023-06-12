package com.naipy.alpha.modules.order.controller;

import com.naipy.alpha.modules.order.models.OrderDTO;
import com.naipy.alpha.modules.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService _orderService;

    @QueryMapping
    @Secured("USER")
    public List<OrderDTO> findAllOrders () {
        return _orderService.findAll();
    }

    @QueryMapping
    @Secured("USER")
    public OrderDTO findByOrderId (@Argument Long id) {
        return _orderService.findById(id);
    }
}
