package com.naipy.alpha.modules.address.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.city.models.City;
import com.naipy.alpha.modules.city.repository.CityRepository;
import com.naipy.alpha.modules.coords.models.Coordinate;
import com.naipy.alpha.modules.country.models.Country;
import com.naipy.alpha.modules.country.repository.CountryRepository;
import com.naipy.alpha.modules.state.models.State;
import com.naipy.alpha.modules.state.repository.StateRepository;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.utils.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.utils.maps.services.MapsService;
import com.naipy.alpha.modules.zipcode.models.ZipCode;
import com.naipy.alpha.modules.zipcode.repository.ZipCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AddressServiceTest {

    @Mock
    CountryRepository _countryRepository;
    @Mock
    StateRepository _stateRepository;
    @Mock
    CityRepository _cityRepository;
    @Mock
    ZipCodeRepository _zipCodeRepository;
    @Mock
    AddressRepository _addressRepository;
    @Mock
    MapsService _mapsService;

    @Autowired
    @InjectMocks
    AddressService _addressService;

    final String postalCodeType = "{ \"results\" : [ { \"address_components\" : [ { \"long_name\" : \"09635-130\", \"short_name\" : \"09635-130\", \"types\" : [ \"postal_code\" ] }, { \"long_name\" : \"Rua Gasparini\", \"short_name\" : \"R. Gasparini\", \"types\" : [ \"route\" ] }, { \"long_name\" : \"Rudge Ramos\", \"short_name\" : \"Rudge Ramos\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"long_name\" : \"São Bernardo do Campo\", \"short_name\" : \"São Bernardo do Campo\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"São Paulo\", \"short_name\" : \"SP\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Brasil\", \"short_name\" : \"BR\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"R. Gasparini - Rudge Ramos, São Bernardo do Campo - SP, 09635-130, Brasil\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : -23.6503873, \"lng\" : -46.57403129999999 }, \"southwest\" : { \"lat\" : -23.65188, \"lng\" : -46.5753147 } }, \"location\" : { \"lat\" : -23.651076, \"lng\" : -46.57465730000001 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : -23.6497846697085, \"lng\" : -46.57332401970849 }, \"southwest\" : { \"lat\" : -23.6524826302915, \"lng\" : -46.57602198029149 } } }, \"place_id\" : \"ChIJWx2WBEhDzpQRT5HESFwEuvY\", \"postcode_localities\" : [ \"Vila Helena\", \"Vila Normandia\" ], \"types\" : [ \"postal_code\" ] } ], \"status\" : \"OK\" }";
    final String streetNumberType = "{ \"results\" : [ { \"address_components\" : [ { \"long_name\" : \"130\", \"short_name\" : \"130\", \"types\" : [ \"street_number\" ] }, { \"long_name\" : \"Rua Gasparini\", \"short_name\" : \"R. Gasparini\", \"types\" : [ \"route\" ] }, { \"long_name\" : \"Vila Normandia\", \"short_name\" : \"Vila Normandia\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"long_name\" : \"São Bernardo do Campo\", \"short_name\" : \"São Bernardo do Campo\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"São Paulo\", \"short_name\" : \"SP\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Brasil\", \"short_name\" : \"BR\", \"types\" : [ \"country\", \"political\" ] }, { \"long_name\" : \"09635-130\", \"short_name\" : \"09635-130\", \"types\" : [ \"postal_code\" ] } ], \"formatted_address\" : \"R. Gasparini, 130 - Vila Normandia, São Bernardo do Campo - SP, 09635-130, Brasil\", \"geometry\" : { \"location\" : { \"lat\" : -23.6509129, \"lng\" : -46.57409550000001 }, \"location_type\" : \"ROOFTOP\", \"viewport\" : { \"northeast\" : { \"lat\" : -23.64962851970849, \"lng\" : -46.5729510197085 }, \"southwest\" : { \"lat\" : -23.6523264802915, \"lng\" : -46.5756489802915 } } }, \"place_id\" : \"ChIJp20cBEhDzpQR9AfLW6MzgFo\", \"plus_code\" : { \"compound_code\" : \"8CXG+J9 Vila Normandia, São Bernardo do Campo - SP, Brasil\", \"global_code\" : \"588M8CXG+J9\" }, \"types\" : [ \"street_address\" ] } ], \"status\" : \"OK\" }";

    @BeforeEach
    void setup () {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addIfDoesntExistsAndGetAddress() throws JsonProcessingException {
        String zipCode = "09635130";
        Optional<Address> falseOptionalAddress = Optional.empty();
        Mockito.when(_addressRepository.findAddressByZipCode(zipCode)).thenReturn(falseOptionalAddress);
        Optional<Address> optionalAddress = _addressRepository.findAddressByZipCode(zipCode);

        if (optionalAddress.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            GeocodeResponse mockedGeocodeResponse = objectMapper.readValue(postalCodeType, GeocodeResponse.class);

            Mockito.when(_mapsService.getAddressBySomethingFromMapsApi(zipCode)).thenReturn(mockedGeocodeResponse);
            GeocodeResponse geocodeResponse = _mapsService.getAddressBySomethingFromMapsApi(zipCode);

            System.out.println(geocodeResponse);
        }
        else
            System.out.println(optionalAddress.get().toString());

    }

    @Test
    void getAddressToUser() {
    }

    @Test
    void instantiateAddressFromGeocodeResponse() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GeocodeResponse geocodeResponse = objectMapper.readValue(postalCodeType, GeocodeResponse.class);
        UserAddress userAddress = new UserAddress();
        Address.AddressBuilder addressBuilder = Address.builder();
        ZipCode zipCode = new ZipCode();
        City city = new City();
        State state = new State();
        Country country = new Country();
        Coordinate coordinate = new Coordinate();

        geocodeResponse.getResults().forEach(addressResult -> {
            addressResult.getAddressComponents().forEach(addressComponent -> {
                if (addressComponent.getTypes().contains("postal_code")) {
                    zipCode.setId(ServiceUtils.generateUUID());
                    zipCode.setCode(addressComponent.getLongName());
                }
                else if (addressComponent.getTypes().contains("route"))
                    addressBuilder.street(addressComponent.getLongName());
                else if (addressComponent.getTypes().contains("sublocality_level_1"))
                    addressBuilder.neighborhood(addressComponent.getLongName());
                else if (addressComponent.getTypes().contains("administrative_area_level_2")) {
                    city.setId(ServiceUtils.generateUUID());
                    city.setName(addressComponent.getLongName());
                    city.setCode(addressComponent.getShortName());
                }
                else if (addressComponent.getTypes().contains("administrative_area_level_1")) {
                    state.setId(ServiceUtils.generateUUID());
                    state.setName(addressComponent.getLongName());
                    state.setCode(addressComponent.getShortName());
                }
                else if (addressComponent.getTypes().contains("country")) {
                    country.setId(ServiceUtils.generateUUID());
                    country.setName(addressComponent.getLongName());
                    country.setCode(addressComponent.getShortName());
                }
            });
            coordinate.setLatitude(addressResult.getGeometry().getLocation().getLat());
            coordinate.setLongitude(addressResult.getGeometry().getLocation().getLng());
        });
        Optional<Country> countryAlreadyExists = _countryRepository.findByName(country.getName());
        Country savedCountry = countryAlreadyExists.orElseGet(() -> _countryRepository.save(country));

        state.setCountry(savedCountry);
        Mockito.when(_stateRepository.save(state)).thenReturn(state);
        Optional<State> stateAlreadyExists = _stateRepository.findByName(state.getName());
        State savedState = stateAlreadyExists.orElseGet(() -> _stateRepository.save(state));

        city.setState(savedState);
        Mockito.when(_cityRepository.save(city)).thenReturn(city);
        Optional<City> cityAlreadyExists = _cityRepository.findByName(city.getName());
        City savedCity = cityAlreadyExists.orElseGet(() -> _cityRepository.save(city));

        //Não é necessário fazer validação do zipCode porque, no método onde invoca esse método, já é feita a busca por zipcode se existe algum endereço cadastrado
        ZipCode savedZipCode = _zipCodeRepository.save(zipCode);
        addressBuilder.id(ServiceUtils.generateUUID());
        addressBuilder.city(savedCity);
        addressBuilder.zipCode(savedZipCode);

        coordinate.setId(ServiceUtils.generateUUID());
        userAddress.setCoordinate(coordinate);
        userAddress.setAddress(addressBuilder.build());
        System.out.println(userAddress.toString());
    }
}