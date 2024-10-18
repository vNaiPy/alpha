package com.naipy.alpha.modules.address.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class AddressDTO {

    private final String id;
    private final String street;
    private final String neighborhood;
    private final String zipcode;
    private final String latitude;
    private final String longitude;
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
