package com.naipy.alpha.modules.utils;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.city.models.City;
import com.naipy.alpha.modules.country.models.Country;
import com.naipy.alpha.modules.state.models.State;

import java.math.BigDecimal;

public class ChargeObject extends ServiceUtils {

    public static final String postalCodeType = "{ \"results\" : [ { \"address_components\" : [ { \"long_name\" : \"09635-130\", \"short_name\" : \"09635-130\", \"types\" : [ \"postal_code\" ] }, { \"long_name\" : \"Rua Gasparini\", \"short_name\" : \"R. Gasparini\", \"types\" : [ \"route\" ] }, { \"long_name\" : \"Rudge Ramos\", \"short_name\" : \"Rudge Ramos\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"long_name\" : \"São Bernardo do Campo\", \"short_name\" : \"São Bernardo do Campo\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"São Paulo\", \"short_name\" : \"SP\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Brasil\", \"short_name\" : \"BR\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"R. Gasparini - Rudge Ramos, São Bernardo do Campo - SP, 09635-130, Brasil\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : -23.6503873, \"lng\" : -46.57403129999999 }, \"southwest\" : { \"lat\" : -23.65188, \"lng\" : -46.5753147 } }, \"location\" : { \"lat\" : -23.651076, \"lng\" : -46.57465730000001 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : -23.6497846697085, \"lng\" : -46.57332401970849 }, \"southwest\" : { \"lat\" : -23.6524826302915, \"lng\" : -46.57602198029149 } } }, \"place_id\" : \"ChIJWx2WBEhDzpQRT5HESFwEuvY\", \"postcode_localities\" : [ \"Vila Helena\", \"Vila Normandia\" ], \"types\" : [ \"postal_code\" ] } ], \"status\" : \"OK\" }";
    public static final String streetNumberType = "{ \"results\" : [ { \"address_components\" : [ { \"long_name\" : \"130\", \"short_name\" : \"130\", \"types\" : [ \"street_number\" ] }, { \"long_name\" : \"Rua Gasparini\", \"short_name\" : \"R. Gasparini\", \"types\" : [ \"route\" ] }, { \"long_name\" : \"Vila Normandia\", \"short_name\" : \"Vila Normandia\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"long_name\" : \"São Bernardo do Campo\", \"short_name\" : \"São Bernardo do Campo\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"São Paulo\", \"short_name\" : \"SP\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Brasil\", \"short_name\" : \"BR\", \"types\" : [ \"country\", \"political\" ] }, { \"long_name\" : \"09635-130\", \"short_name\" : \"09635-130\", \"types\" : [ \"postal_code\" ] } ], \"formatted_address\" : \"R. Gasparini, 130 - Vila Normandia, São Bernardo do Campo - SP, 09635-130, Brasil\", \"geometry\" : { \"location\" : { \"lat\" : -23.6509129, \"lng\" : -46.57409550000001 }, \"location_type\" : \"ROOFTOP\", \"viewport\" : { \"northeast\" : { \"lat\" : -23.64962851970849, \"lng\" : -46.5729510197085 }, \"southwest\" : { \"lat\" : -23.6523264802915, \"lng\" : -46.5756489802915 } } }, \"place_id\" : \"ChIJp20cBEhDzpQR9AfLW6MzgFo\", \"plus_code\" : { \"compound_code\" : \"8CXG+J9 Vila Normandia, São Bernardo do Campo - SP, Brasil\", \"global_code\" : \"588M8CXG+J9\" }, \"types\" : [ \"street_address\" ] } ], \"status\" : \"OK\" }";
    public static final String notFoundAddressFromMaps = "{ \"results\" : [], \"status\" : \"ZERO_RESULTS\" }";
    public static final String apiKeyInvalid = "{ \"error_message\" : \"The provided API key is invalid. \", \"results\" : [], \"status\" : \"REQUEST_DENIED\" }";

    public static Country getOneCountry () {
        return Country.builder()
                .id(generateUUID())
                .name("Brasil")
                .code("BR")
                .build();
    }

    public static State getOneState () {
        return State.builder()
                .id(generateUUID())
                .name("São Paulo")
                .country(getOneCountry())
                .code("SP")
                .build();
    }

    public static City getOneCity () {
        return City.builder()
                .id(generateUUID())
                .name("São Bernardo do Campo")
                .code("São Bernardo do Campo")
                .state(getOneState())
                .build();
    }

    public static Address getOneAddress () {
        return Address.builder()
                .id(generateUUID())
                .street("Rua Gasparini")
                .neighborhood("Vila Normandia")
                .zipcode("09635130")
                .city(getOneCity())
                .latitude(BigDecimal.valueOf(-23.6509129))
                .longitude(BigDecimal.valueOf(-46.57409550000001))
                .build();
    }

    public static AddressEnriched getOneAddressEnriched () {
        return AddressEnriched.builder()
                .address(getOneAddress())
                .streetNumber("130")
                .build();
    }
}
