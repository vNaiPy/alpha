package com.naipy.alpha.modules.order.models;

import com.naipy.alpha.modules.order_item.model.OrderItemDTO;
import com.naipy.alpha.modules.user.models.UserDTO;
import com.naipy.alpha.modules.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

    private String id;
    private Instant moment;
    private OrderStatus orderStatus;
    private UserDTO userDTO;
    private Set<OrderItemDTO> items = new HashSet<>();
    private Payment payment;

    public static OrderDTO createOrderDTO (Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .moment(order.getMoment())
                .orderStatus(order.getOrderStatus())
                .userDTO(UserDTO.createUserDTO(order.getUser()))
                .items(order.getItems().stream().map(OrderItemDTO::new).collect(Collectors.toSet()))
                .payment(order.getPayment())
                .build();
    }
}
