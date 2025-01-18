package com.naipy.alpha.modules.address.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.utils.ChargeAddressObject;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.external_api.maps.services.MapsService;
import org.junit.jupiter.api.Assertions;
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

class AddressServiceTest extends ServiceUtils {

    @Mock
    AddressRepository addressRepository;
    @Mock
    MapsService mapsService;

    @Autowired
    @InjectMocks
    AddressService addressService;

    @BeforeEach
    void setup () {
        MockitoAnnotations.openMocks(this);
    }

    static List<String> myParameters() {
        return List.of(ChargeAddressObject.POSTAL_CODE_TYPE, ChargeAddressObject.STREET_NUMBER_TYPE);
    }

    @Test
    void getAddressAndAddIfDoesntExists() throws JsonProcessingException {
        String zipCode = "09635130";
        Optional<Address> falseOptionalAddress = Optional.empty();
        Mockito.when(addressRepository.findAddressByZipCode(zipCode)).thenReturn(falseOptionalAddress);
        Optional<Address> optionalAddress = addressRepository.findAddressByZipCode(zipCode);

        if (optionalAddress.isEmpty()) {
            GeocodeResponse mockedGeocodeResponse = ChargeAddressObject.getPostalCodeType();
            Mockito.when(mapsService.getAddressBy(zipCode)).thenReturn(mockedGeocodeResponse);
            GeocodeResponse geocodeResponse = mapsService.getAddressBy(zipCode);
            addressService.isValidGeocodeResponse(geocodeResponse, zipCode);

            AddressEnriched addressEnriched = addressService.instantiateAddressEnrichedFromGeocodeResponse(geocodeResponse);

            Mockito.when(addressRepository.save(addressEnriched.getAddress())).thenReturn(addressEnriched.getAddress());
            AddressDTO addressDTO = new AddressDTO(addressRepository.save(addressEnriched.getAddress()));

            Address addressGoal =  ChargeAddressObject.getOnePostalCode();
            equalizerObjectId(addressEnriched.getAddress(), addressGoal);
            AddressDTO successGoal = new AddressDTO(addressGoal);
            Assertions.assertEquals(addressDTO, successGoal);
        }
        else {
            Assertions.assertFalse(optionalAddress.isEmpty());
        }
    }


    @ParameterizedTest
    @MethodSource("myParameters")
    void instantiateAddressFromGeocodeResponse(String geocodeResponseParam) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GeocodeResponse geocodeResponse = objectMapper.readValue(geocodeResponseParam, GeocodeResponse.class);
        AddressEnriched addressEnriched = addressService.instantiateAddressEnrichedFromGeocodeResponse(geocodeResponse);
        if (Optional.ofNullable(addressEnriched.getStreetNumber()).isPresent()){
            AddressEnriched addressEnrichedGoal =  ChargeAddressObject.getOneAddressEnriched();
            equalizerObjectId(addressEnriched, addressEnrichedGoal);
            Assertions.assertEquals(addressEnriched, addressEnrichedGoal);
        }
        else {
            Address addressGoal =  ChargeAddressObject.getOnePostalCode();
            equalizerObjectId(addressEnriched.getAddress(), addressGoal);
            Assertions.assertEquals(addressEnriched.getAddress(), addressGoal);
        }
    }

    void equalizerObjectId(Address address, Address addressGoal) {
        addressGoal.setId(address.getId());
    }

    void equalizerObjectId(AddressEnriched addressEnriched, AddressEnriched addressEnrichedGoal) {
        addressEnrichedGoal.getAddress().setId(addressEnriched.getAddress().getId());
    }
}