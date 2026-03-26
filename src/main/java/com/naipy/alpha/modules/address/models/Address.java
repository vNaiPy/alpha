package com.naipy.alpha.modules.address.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
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

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @Transient
    private String streetNumber;

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

    /**
     * @param geocodeResponse eh um objeto mapeado com dados retornados do GoogleMapsAPI
     * @return Address eh um objeto que possui o mapeamento de informacoes necessarias para relacionar endereco e usuario.
     */
    public static Address getAddressFromGeocodeResponse (GeocodeResponse geocodeResponse) {
        Address.AddressBuilder addressBuilder = Address.builder();
        final String postalCodeKey = "postal_code";
        final String streetKey = "route";
        final String neighborhoodKey = "sublocality_level_1";
        final String cityKey = "administrative_area_level_2";
        final String stateKey = "administrative_area_level_1";
        final String countryKey = "country";
        final String streetNumberKey = "street_number";

        geocodeResponse.getResults().forEach(addressResult -> {
            addressResult.getAddressComponents().forEach(addressComponent -> {
                List<String> componentTypes = addressComponent.getTypes();
                if (componentTypes.contains(postalCodeKey))
                    addressBuilder.zipcode(ServiceUtils.removeNonNumeric(addressComponent.getLongName()));
                else if (componentTypes.contains(streetKey))
                    addressBuilder.street(addressComponent.getLongName());
                else if (componentTypes.contains(neighborhoodKey))
                    addressBuilder.neighborhood(addressComponent.getLongName());
                else if (componentTypes.contains(cityKey))
                    addressBuilder.city(addressComponent.getLongName());
                else if (componentTypes.contains(stateKey))
                    addressBuilder.state(addressComponent.getLongName());
                else if (componentTypes.contains(countryKey))
                    addressBuilder.country(addressComponent.getLongName());
                else if (componentTypes.contains(streetNumberKey))
                    addressBuilder.streetNumber(addressComponent.getLongName());
            });
            addressBuilder.latitude(addressResult.getGeometry().getLocation().getLat());
            addressBuilder.longitude(addressResult.getGeometry().getLocation().getLng());
        });

        addressBuilder.id(ServiceUtils.generateUUID());
        return addressBuilder.build();
    }


}
