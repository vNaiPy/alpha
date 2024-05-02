package com.naipy.alpha.modules.address.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.city.models.City;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.utils.maps.models.AddressComponent;
import com.naipy.alpha.modules.utils.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.zipcode.models.ZipCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
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

    @NotNull
    private String street;

    @NotNull
    private String neighborhood;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToOne
    @JoinColumn(name = "zipcode_id")
    private ZipCode zipCode;

    @JsonIgnore
    @OneToMany(mappedBy = "id.address")
    @ToString.Exclude
    private Set<UserAddress> usersAddresses = new HashSet<>();
}
