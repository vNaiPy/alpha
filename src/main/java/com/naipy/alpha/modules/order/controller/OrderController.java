package com.naipy.alpha.modules.order.controller;

import com.naipy.alpha.modules.order.models.OrderDTO;
import com.naipy.alpha.modules.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService _orderService;

    @Autowired
    public OrderController (OrderService orderService) {
        this._orderService = orderService;
    }

    @QueryMapping
    @Secured("USER")
    public List<OrderDTO> findAllOrders () {
        return _orderService.findAll();
    }

    @QueryMapping
    @Secured("USER")
    public OrderDTO findByOrderId (@Argument String id) {
        return _orderService.findById(id);
    }
}
