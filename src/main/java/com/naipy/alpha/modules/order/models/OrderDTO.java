package com.naipy.alpha.modules.order.models;

import com.naipy.alpha.modules.order_item.model.OrderItemDTO;
import com.naipy.alpha.modules.auth.models.UserDTO;
import com.naipy.alpha.modules.order.enums.OrderStatus;
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
