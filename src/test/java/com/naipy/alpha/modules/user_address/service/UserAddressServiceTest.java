package com.naipy.alpha.modules.user_address.service;

import com.naipy.alpha.modules.address.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserAddressServiceTest {

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
    }
}