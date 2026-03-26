package com.naipy.alpha.modules.address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.exceptions.services.ExternalResponseNotReceivedException;
import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.external_api.maps.services.MapsService;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService  {

    private final AddressRepository addressRepository;
    private final MapsService mapsService;

    @Autowired
    public AddressService(AddressRepository addressRepository, MapsService mapsService) {
        this.addressRepository = addressRepository;
        this.mapsService = mapsService;
    }

    /**
     * Primeiro, consultar no Redis. Caso nao exista, consultar no Postgre. Caso nao exista, consultar na API GoogleMaps e salvar nos bancos.
     * @param zipCode eh um codigo postal/CEP
     * @return Address - Retorna um endereco salvo no banco
     */
    public AddressDTO getAddressAndAddIfDoesntExists (String zipCode) {
        AddressDTO addressDTO;
        zipCode = ServiceUtils.removeNonNumeric(zipCode);
        Optional<Address> optionalAddress = addressRepository.findAddressByZipcode(zipCode);
        if (optionalAddress.isEmpty()) {
            GeocodeResponse geocodeResponse =
                    mapsService.getAddressBy(zipCode);
            addressDTO = new AddressDTO(addressRepository.save(Address.getAddressFromGeocodeResponse(geocodeResponse)));
        }
        else {
            addressDTO = new AddressDTO(optionalAddress.get());
        }
        return addressDTO;
    }

    public Address getAddressByCompleteAddress (String completeAddress) {
        GeocodeResponse geocodeResponse = mapsService.getAddressBy(completeAddress);
        return Address.getAddressFromGeocodeResponse(geocodeResponse);
    }
}
