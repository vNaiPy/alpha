package com.naipy.alpha.modules.user_address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.service.AddressService;
import com.naipy.alpha.modules.exceptions.services.InvalidParameterException;
import com.naipy.alpha.modules.utils.ChargeObject;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserAddressServiceTest extends ServiceUtils {

    @Mock
    AddressService _addressService;

    @InjectMocks
    UserAddressService _userAddressService;

    @BeforeEach
    void setup () {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void addAddressToUser() {
        String zipCode = "09635-130";
        String streetNumber = "130";
        Mockito.when(_addressService.getAddressAndAddIfDoesntExists(zipCode)).thenReturn(new AddressDTO(ChargeObject.getOneAddress()));
        AddressDTO addressDTO = _addressService.getAddressAndAddIfDoesntExists(zipCode);

        Mockito.when(_userAddressService.getExactAddressOfUser(addressDTO, streetNumber)).thenReturn(ChargeObject.getOneAddressEnriched());
        AddressEnriched addressEnriched = _userAddressService.getExactAddressOfUser(addressDTO, streetNumber);
        System.out.println(addressEnriched);
    }

    @Test
    void addAddressToUserThrowingInvalidParameterException() {
        String zipCode = "09635-130";
        String streetNumber = "130";
        /*Mockito.when(_addressService.getAddressAndAddIfDoesntExists(zipCode)).thenReturn(ChargeObject.getOneAddress());
        Address address = _addressService.getAddressAndAddIfDoesntExists(zipCode);
        String addressComplete = address.getStreet() + WHITESPACE
                + streetNumber + WHITESPACE
                + address.getNeighborhood() + WHITESPACE
                + address.getCity().getName() + WHITESPACE
                + address.getCity().getState().getName() + WHITESPACE
                + address.getCity().getState().getCountry().getName();

        if (addressComplete.isBlank())
            throw new InvalidParameterException(addressComplete + ". This is not valid for searching in the MapsAPI");
*/
    }
}