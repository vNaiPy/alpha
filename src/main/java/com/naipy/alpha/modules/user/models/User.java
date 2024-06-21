package com.naipy.alpha.modules.user.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.store.model.Store;
import com.naipy.alpha.modules.token.Token;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_users")
public class User implements UserDetails {

    @Id
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String profilePicture;

    @NotBlank
    private String password;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant registeredSince;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant lastUpdate;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Store store;

    @OneToMany(mappedBy = "id.user")
    private Set<UserAddress> usersAddresses = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Token> tokenList;

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}