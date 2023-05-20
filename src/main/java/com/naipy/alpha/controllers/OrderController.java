package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Order;
import com.naipy.alpha.entities.dto.OrderDTO;
import com.naipy.alpha.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService _orderService;

    @QueryMapping
    public List<OrderDTO> findAllOrders () {
        return _orderService.findAll();
    }

    @QueryMapping
    public Order findByOrderId (@Argument Long id) {
        return _orderService.findById(id);
    }
}
