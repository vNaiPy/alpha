package com.naipy.alpha.modules.address.controller;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private final AddressService _addressService;

    @QueryMapping()
    public Address findByZipCode (@Argument String zipCode) {return _addressService.addOrGetAddress(zipCode);}

}
