package com.naipy.alpha.modules.external_api.maps.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

class MapsServiceTest {

    /*final String postalCodeType = "{ \"results\" : [ { \"address_components\" : [ { \"long_name\" : \"09635-130\", \"short_name\" : \"09635-130\", \"types\" : [ \"postal_code\" ] }, { \"long_name\" : \"Rua Gasparini\", \"short_name\" : \"R. Gasparini\", \"types\" : [ \"route\" ] }, { \"long_name\" : \"Rudge Ramos\", \"short_name\" : \"Rudge Ramos\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"long_name\" : \"São Bernardo do Campo\", \"short_name\" : \"São Bernardo do Campo\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"São Paulo\", \"short_name\" : \"SP\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Brasil\", \"short_name\" : \"BR\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"R. Gasparini - Rudge Ramos, São Bernardo do Campo - SP, 09635-130, Brasil\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : -23.6503873, \"lng\" : -46.57403129999999 }, \"southwest\" : { \"lat\" : -23.65188, \"lng\" : -46.5753147 } }, \"location\" : { \"lat\" : -23.651076, \"lng\" : -46.57465730000001 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : -23.6497846697085, \"lng\" : -46.57332401970849 }, \"southwest\" : { \"lat\" : -23.6524826302915, \"lng\" : -46.57602198029149 } } }, \"place_id\" : \"ChIJWx2WBEhDzpQRT5HESFwEuvY\", \"postcode_localities\" : [ \"Vila Helena\", \"Vila Normandia\" ], \"types\" : [ \"postal_code\" ] } ], \"status\" : \"OK\" }";
    final String streetNumberType = "{ \"results\" : [ { \"address_components\" : [ { \"long_name\" : \"130\", \"short_name\" : \"130\", \"types\" : [ \"street_number\" ] }, { \"long_name\" : \"Rua Gasparini\", \"short_name\" : \"R. Gasparini\", \"types\" : [ \"route\" ] }, { \"long_name\" : \"Vila Normandia\", \"short_name\" : \"Vila Normandia\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"long_name\" : \"São Bernardo do Campo\", \"short_name\" : \"São Bernardo do Campo\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"São Paulo\", \"short_name\" : \"SP\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Brasil\", \"short_name\" : \"BR\", \"types\" : [ \"country\", \"political\" ] }, { \"long_name\" : \"09635-130\", \"short_name\" : \"09635-130\", \"types\" : [ \"postal_code\" ] } ], \"formatted_address\" : \"R. Gasparini, 130 - Vila Normandia, São Bernardo do Campo - SP, 09635-130, Brasil\", \"geometry\" : { \"location\" : { \"lat\" : -23.6509129, \"lng\" : -46.57409550000001 }, \"location_type\" : \"ROOFTOP\", \"viewport\" : { \"northeast\" : { \"lat\" : -23.64962851970849, \"lng\" : -46.5729510197085 }, \"southwest\" : { \"lat\" : -23.6523264802915, \"lng\" : -46.5756489802915 } } }, \"place_id\" : \"ChIJp20cBEhDzpQR9AfLW6MzgFo\", \"plus_code\" : { \"compound_code\" : \"8CXG+J9 Vila Normandia, São Bernardo do Campo - SP, Brasil\", \"global_code\" : \"588M8CXG+J9\" }, \"types\" : [ \"street_address\" ] } ], \"status\" : \"OK\" }";

    final String WHITESPACE_CODE = "%20";

    @Autowired
    @InjectMocks
    MapsService mapsService;

    @BeforeEach
    void setup () {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAddressBySomethingFromMapsApi_Cep() {
        String searchFor = "09635130";
        GeocodeResponse geocodeResponse = mapsService.getAddressBy(searchFor);
        System.out.println(geocodeResponse);
    }

    @Test
    void getAddressBySomethingFromMapsApi_Logradouro() {
        String searchFor =
                "Rua Gasparini 130 Rudge Ramos São Bernardo do Campo Sao Paulo Brasil"
                .replace(" ", WHITESPACE_CODE);
        GeocodeResponse geocodeResponse = mapsService.getAddressBy(searchFor);
        System.out.println(geocodeResponse);
    }

    @Test
    void getAddressBySomethingFromMapsApi_CepWithMockRequest() throws JsonProcessingException {
        String searchFor = "09635130";

        ObjectMapper objectMapper = new ObjectMapper();
        GeocodeResponse geocodeResponse = objectMapper.readValue(postalCodeType, GeocodeResponse.class);

        System.out.println(geocodeResponse.toString());
    }

    @Test
    void getAddressBySomethingFromMapsApi_LogradouroWithMockRequest() throws JsonProcessingException {
        String searchFor =
                "Rua Gasparini 130 Rudge Ramos São Bernardo do Campo Sao Paulo Brasil"
                .replace(" ", WHITESPACE_CODE);
        ObjectMapper objectMapper = new ObjectMapper();
        GeocodeResponse geocodeResponse = objectMapper.readValue(streetNumberType, GeocodeResponse.class);

        System.out.println(geocodeResponse.toString());
    }*/
}