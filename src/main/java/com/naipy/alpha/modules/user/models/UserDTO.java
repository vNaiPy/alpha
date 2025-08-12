package com.naipy.alpha.modules.user.models;

import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.store.models.Store;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user_address.models.UserAddress;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public record UserDTO (
    String id,
    String username,
    String name,
    String surname,
    String email,
    String phone,
    Store store,
    UserStatus status,
    String pictureUrl,
    Instant createdAt,
    String identityDocument,
    List<Role> roles,
    Set<UserAddress> usersAddresses,
    List<Order> orders
) implements Serializable {
    public static UserDTO createUserDTO (User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhone(),
                user.getStore(),
                user.getStatus(),
                user.getPictureUrl(),
                user.getCreatedAt(),
                user.getIdentityDocument(),
                user.getRoles(),
                user.getUsersAddresses(),
                user.getOrders()
        );
    }
}
