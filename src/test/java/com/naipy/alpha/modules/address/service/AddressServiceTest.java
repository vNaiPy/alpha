package com.naipy.alpha.modules.address.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.city.models.City;
import com.naipy.alpha.modules.city.repository.CityRepository;
import com.naipy.alpha.modules.country.models.Country;
import com.naipy.alpha.modules.country.repository.CountryRepository;
import com.naipy.alpha.modules.state.models.State;
import com.naipy.alpha.modules.state.repository.StateRepository;
import com.naipy.alpha.modules.utils.ChargeObject;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.external_api.maps.services.MapsService;
import com.naipy.alpha.modules.zipcode.models.ZipCode;
import com.naipy.alpha.modules.zipcode.repository.ZipCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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

    @BeforeEach
    void setup () {
        MockitoAnnotations.openMocks(this);
    }

    static List<String> myParameters() {
        return List.of(ChargeObject.postalCodeType, ChargeObject.streetNumberType);
    }

    @Test
    void getAddressAndAddIfDoesntExists() throws JsonProcessingException {
        String zipCode = "09635130";
        Optional<Address> falseOptionalAddress = Optional.empty();
        Mockito.when(_addressRepository.findAddressByZipCode(zipCode)).thenReturn(falseOptionalAddress);
        Optional<Address> optionalAddress = _addressRepository.findAddressByZipCode(zipCode);

        if (optionalAddress.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            GeocodeResponse mockedGeocodeResponse = objectMapper.readValue(ChargeObject.postalCodeType, GeocodeResponse.class);

            Mockito.when(_mapsService.getAddressByZipCodeOrCompleteAddressFromMapsApi(zipCode)).thenReturn(mockedGeocodeResponse);
            GeocodeResponse geocodeResponse = _mapsService.getAddressByZipCodeOrCompleteAddressFromMapsApi(zipCode);
            System.out.println(geocodeResponse);
        }
        else
            System.out.println(optionalAddress.get().toString());
    }


    @ParameterizedTest
    @MethodSource("myParameters")
    void instantiateAddressFromGeocodeResponse(String geocodeResponseParam) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GeocodeResponse geocodeResponse = objectMapper.readValue(geocodeResponseParam, GeocodeResponse.class);
        Address.AddressBuilder addressBuilder = Address.builder();
        AddressEnriched.AddressEnrichedBuilder addressEnrichedBuilder = AddressEnriched.builder();
        ZipCode zipCode = new ZipCode();
        City city = new City();
        State state = new State();
        Country country = new Country();

        geocodeResponse.getResults().forEach(addressResult -> {
            addressResult.getAddressComponents().forEach(addressComponent -> {
                List<String> componentTypes = addressComponent.getTypes();
                if (componentTypes.contains("postal_code")) {
                    zipCode.setId(ServiceUtils.generateUUID());
                    zipCode.setCode(addressComponent.getLongName());
                }
                else if (componentTypes.contains("route"))
                    addressBuilder.street(addressComponent.getLongName());
                else if (componentTypes.contains("sublocality_level_1"))
                    addressBuilder.neighborhood(addressComponent.getLongName());
                else if (componentTypes.contains("administrative_area_level_2")) {
                    city.setId(ServiceUtils.generateUUID());
                    city.setName(addressComponent.getLongName());
                    city.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("administrative_area_level_1")) {
                    state.setId(ServiceUtils.generateUUID());
                    state.setName(addressComponent.getLongName());
                    state.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("country")) {
                    country.setId(ServiceUtils.generateUUID());
                    country.setName(addressComponent.getLongName());
                    country.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("street_number"))
                    addressEnrichedBuilder.streetNumber(addressComponent.getLongName());
            });
            addressBuilder.latitude(addressResult.getGeometry().getLocation().getLat());
            addressBuilder.longitude(addressResult.getGeometry().getLocation().getLng());
        });
        Mockito.when(_countryRepository.save(country)).thenReturn(country);
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
        Mockito.when(_zipCodeRepository.save(zipCode)).thenReturn(zipCode);
        ZipCode savedZipCode = _zipCodeRepository.save(zipCode);
        addressBuilder.id(ServiceUtils.generateUUID());
        addressBuilder.city(savedCity);
        addressBuilder.zipCode(savedZipCode);

        addressEnrichedBuilder.address(addressBuilder.build());
        System.out.println(addressEnrichedBuilder.build().toString());
    }
}