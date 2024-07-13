package com.naipy.alpha.modules.user_address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.service.AddressService;
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
        Mockito.when(_addressService.getAddressAndAddIfDoesntExists(zipCode)).thenReturn(ChargeObject.getOneAddress());
        Address address = _addressService.getAddressAndAddIfDoesntExists(zipCode);
        String addressComplete = address.getStreet()
                + streetNumber
                + address.getNeighborhood()
                + address.getCity().getName()
                + address.getCity().getState().getName()
                + address.getCity().getState().getCountry().getName();

        System.out.println(addressComplete.toString());

    }
}