package com.naipy.alpha.modules.address.models;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@ToString
public class AddressDTO {

    private final UUID id;
    private final String street;
    private final String neighborhood;
    private final String zipcode;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final String city;
    private final String state;
    private final String country;

    public AddressDTO (Address address) {
        this.id = address.getId();
        this.street = address.getStreet();
        this.neighborhood = address.getNeighborhood();
        this.zipcode = address.getZipcode();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
        this.city = address.getCity();
        this.state = address.getState();
        this.country = address.getCountry();
    }
}
