package com.naipy.alpha.modules.address.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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

    @Serial
    private static final long serialVersionUID = UniversalSerialVersion.ADDRESS_SERIAL_VERSION_UID;

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @NotBlank
    private String street;

    @NotBlank
    private String neighborhood;

    @NotBlank
    @Column(length = 12, nullable = false, unique = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) && Objects.equals(street, address.street) && Objects.equals(neighborhood, address.neighborhood) && Objects.equals(zipcode, address.zipcode) && Objects.equals(city, address.city) && Objects.equals(state, address.state) && Objects.equals(country, address.country) && Objects.equals(latitude, address.latitude) && Objects.equals(longitude, address.longitude) && Objects.equals(usersAddresses, address.usersAddresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, neighborhood, zipcode, city, state, country, latitude, longitude, usersAddresses);
    }
}
