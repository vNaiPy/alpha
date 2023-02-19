package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Order;
import com.naipy.alpha.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderService _orderService;

    @GetMapping
    public ResponseEntity<List<Order>> findAll () {
        List<Order> orderList = _orderService.findAll();
        return ResponseEntity.ok().body(orderList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> findById (@PathVariable Long id) {
        Order order = _orderService.findById(id);
        return ResponseEntity.ok().body(order);
    }

}
