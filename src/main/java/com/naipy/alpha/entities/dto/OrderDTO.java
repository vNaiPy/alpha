package com.naipy.alpha.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.naipy.alpha.entities.Order;
import com.naipy.alpha.entities.OrderItem;
import com.naipy.alpha.entities.Payment;
import com.naipy.alpha.entities.User;
import com.naipy.alpha.entities.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private Instant moment;
    private OrderStatus orderStatus;
    private UserDTO client;
    private Set<OrderItemDTO> items = new HashSet<>();
    private Payment payment;

    public static OrderDTO createOrderDTO (Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .moment(order.getMoment())
                .orderStatus(order.getOrderStatus())
                .client(UserDTO.createUserDTO(order.getClient()))
                .items(order.getItems().stream().map(OrderItemDTO::new).collect(Collectors.toSet()))
                .payment(order.getPayment())
                .build();
    }
}
