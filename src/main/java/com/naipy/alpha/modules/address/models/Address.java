package com.naipy.alpha.modules.address.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
    private String id;

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
    private String latitude;

    @NotBlank
    private String longitude;

    @JsonIgnore
    @OneToMany(mappedBy = "id.address")
    @ToString.Exclude
    private Set<UserAddress> usersAddresses = new HashSet<>();
}
