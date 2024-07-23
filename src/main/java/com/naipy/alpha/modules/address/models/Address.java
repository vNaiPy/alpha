package com.naipy.alpha.modules.address.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_addresses")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {

    @Id
    private UUID id;

    @NotBlank
    private String street;

    @NotBlank
    private String neighborhood;

    @NotBlank
    @Column(length = 12)
    private String zipcode;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String country;

    @NotBlank
    private BigDecimal latitude;

    @NotBlank
    private BigDecimal longitude;

    @JsonIgnore
    @OneToMany(mappedBy = "id.address")
    @ToString.Exclude
    private Set<UserAddress> usersAddresses = new HashSet<>();
}
