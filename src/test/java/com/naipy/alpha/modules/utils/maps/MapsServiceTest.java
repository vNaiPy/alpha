package com.naipy.alpha.modules.utils.maps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;


public class MapsServiceTest {

    @Autowired
    @InjectMocks
    MapsService mapsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAddressFromMapsApi() {
        System.out.println(mapsService.getAddressFromMapsApi("Rua%20Gasparini%20130%20Rudge%20Ramos").toString());
    }

    @Test
    void getAddressByPostalCode() {
        System.out.println(mapsService.getAddressFromMapsApi("09635130").toString());
    }
}