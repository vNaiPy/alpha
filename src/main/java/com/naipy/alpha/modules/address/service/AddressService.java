package com.naipy.alpha.modules.address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.city.repository.CityRepository;
import com.naipy.alpha.modules.country.models.Country;
import com.naipy.alpha.modules.country.repository.CountryRepository;
import com.naipy.alpha.modules.state.repository.StateRepository;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.utils.maps.MapsService;
import com.naipy.alpha.modules.zipcode.repository.ZipCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    CountryRepository _countryRepository;
    @Autowired
    StateRepository _stateRepository;
    @Autowired
    CityRepository _cityRepository;
    @Autowired
    ZipCodeRepository _zipCodeRepository;
    @Autowired
    AddressRepository _addressRepository;
    @Autowired
    MapsService _mapsService;

    final String MAPS_KEY = "AIzaSyDLr4j7hVxfeYDR1wEC1YnDSgw91UqOjsY";
    final String WHITESPACE = "%20";

    public Address addAddress (Address address) {
        Optional<Address> optionalAddress = _addressRepository.findAddressByZipCode(address.getZipCode().getCode());

        if (optionalAddress.isEmpty()) {
            String addressForRequest = address.getStreet() + address.get;
            _mapsService.getAddressFromMapsApi(addressForRequest);
            Address adrs = Address.builder()
                    .id(ServiceUtils.generateUUID())
                    .build();
            return _addressRepository.save(address);
        }
        return optionalAddress.get();
    }

}
