package com.naipy.alpha.modules.user.models;

import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.store.model.Store;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String name;
    private String email;
    private String phone;
    private Store store;
    private UserStatus status;
    private Set<UserAddress> usersAddresses = new HashSet<>();
    private List<Order> orders = new ArrayList<>();


    public static UserDTO createUserDTO (User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .store(user.getStore())
                .status(user.getStatus())
                .usersAddresses(user.getUsersAddresses())
                .orders(user.getOrders())
                .build();
    }
}
