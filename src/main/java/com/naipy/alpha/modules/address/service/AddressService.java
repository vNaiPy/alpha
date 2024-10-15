package com.naipy.alpha.modules.address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.exceptions.services.ExternalResponseNotReceivedException;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.external_api.maps.services.MapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService extends ServiceUtils{

    @Autowired
    AddressRepository addressRepository;
    
    @Autowired
    MapsService mapsService;

    /**
     * Primeiro, consultar no Redis. Caso nao exista, consultar no Postgrees. Caso nao exista, consultar na API GoogleMaps e salvar nos bancos.
     * @param zipCode eh um codigo postal/CEP
     * @return Address - Retorna um endereco salvo no banco
     */
    public AddressDTO getAddressAndAddIfDoesntExists (String zipCode) {
        zipCode = removeNonNumeric(zipCode);
        Optional<Address> optionalAddress = addressRepository.findAddressByZipCode(zipCode);
        if (optionalAddress.isEmpty()) {
            GeocodeResponse geocodeResponse =
                    mapsService.getAddressByZipCodeOrCompleteAddressFromMapsApi(zipCode);
            isValidGeocodeResponse(geocodeResponse, zipCode);
            return new AddressDTO(addressRepository.save(instantiateAddressEnrichedFromGeocodeResponse(geocodeResponse).getAddress()));
        }
        else
            return new AddressDTO(optionalAddress.get());
    }

    public AddressEnriched getAddressEnrichedByCompleteAddress (String completeAddress) {
        GeocodeResponse geocodeResponse = mapsService.getAddressByZipCodeOrCompleteAddressFromMapsApi(completeAddress);
        isValidGeocodeResponse(geocodeResponse, completeAddress);
        return instantiateAddressEnrichedFromGeocodeResponse(geocodeResponse);
    }

    public void isValidGeocodeResponse (GeocodeResponse geocodeResponse, String zipCodeOrCompleteAddress) {
        if (geocodeResponse.getStatus().equals("ZERO_RESULTS"))
            throw new ExternalResponseNotReceivedException("Status: " + geocodeResponse.getStatus() + ". Parameter passed: " + zipCodeOrCompleteAddress);
        else if (isDifferent("OK", geocodeResponse.getStatus()))
            throw new ExternalResponseNotReceivedException("Status: " + geocodeResponse.getStatus() + ". External error received: " + geocodeResponse.getErrorMessage());
    }

    /**
     * @param geocodeResponse eh um objeto mapeado com dados retornados do GoogleMapsAPI
     * @return AddressEnriched eh um objeto que possui o mapeamento de informacoes necessarias para relacionar endereco e usuario.
     */
    public AddressEnriched instantiateAddressEnrichedFromGeocodeResponse (GeocodeResponse geocodeResponse) {
        Address.AddressBuilder addressBuilder = Address.builder();
        AddressEnriched.AddressEnrichedBuilder addressEnrichedBuilder = AddressEnriched.builder();

        geocodeResponse.getResults().forEach(addressResult -> {
            addressResult.getAddressComponents().forEach(addressComponent -> {
                List<String> componentTypes = addressComponent.getTypes();
                if (componentTypes.contains("postal_code"))
                    addressBuilder.zipcode(removeNonNumeric(addressComponent.getLongName()));
                else if (componentTypes.contains("route"))
                    addressBuilder.street(addressComponent.getLongName());
                else if (componentTypes.contains("sublocality_level_1"))
                    addressBuilder.neighborhood(addressComponent.getLongName());
                else if (componentTypes.contains("administrative_area_level_2"))
                    addressBuilder.city(addressComponent.getLongName());
                else if (componentTypes.contains("administrative_area_level_1"))
                    addressBuilder.state(addressComponent.getLongName());
                else if (componentTypes.contains("country"))
                    addressBuilder.country(addressComponent.getLongName());
                else if (componentTypes.contains("street_number"))
                    addressEnrichedBuilder.streetNumber(addressComponent.getLongName());
            });
            addressBuilder.latitude(addressResult.getGeometry().getLocation().getLat());
            addressBuilder.longitude(addressResult.getGeometry().getLocation().getLng());
        });

        addressBuilder.id(generateUUID());
        addressEnrichedBuilder.address(addressBuilder.build());
        return addressEnrichedBuilder.build();
    }
}
