package com.naipy.alpha.modules.address.controller;

import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @QueryMapping()
    public AddressDTO findByZipcode (@Argument String zipcode) {return addressService.getAddressAndAddIfDoesntExists(zipcode);}

}
