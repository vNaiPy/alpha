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
public class AddressService extends ServiceUtils {

    private final AddressRepository addressRepository;
    private final MapsService mapsService;

    @Autowired
    public AddressService(AddressRepository addressRepository, MapsService mapsService) {
        this.addressRepository = addressRepository;
        this.mapsService = mapsService;
    }

    /**
     * Primeiro, consultar no Redis. Caso nao exista, consultar no Postgrees. Caso nao exista, consultar na API GoogleMaps e salvar nos bancos.
     * @param zipCode eh um codigo postal/CEP
     * @return Address - Retorna um endereco salvo no banco
     */
    public AddressDTO getAddressAndAddIfDoesntExists (String zipCode) {
        AddressDTO addressDTO;
        zipCode = removeNonNumeric(zipCode);
        Optional<Address> optionalAddress = addressRepository.findAddressByZipCode(zipCode);
        if (optionalAddress.isEmpty()) {
            GeocodeResponse geocodeResponse =
                    mapsService.getAddressBy(zipCode);
            isValidGeocodeResponse(geocodeResponse, zipCode);
            addressDTO = new AddressDTO(addressRepository.save(instantiateAddressEnrichedFromGeocodeResponse(geocodeResponse).getAddress()));
        }
        else {
            addressDTO = new AddressDTO(optionalAddress.get());
        }
        return addressDTO;
    }

    public AddressEnriched getAddressEnrichedByCompleteAddress (String completeAddress) {
        GeocodeResponse geocodeResponse = mapsService.getAddressBy(completeAddress);
        isValidGeocodeResponse(geocodeResponse, completeAddress);
        return instantiateAddressEnrichedFromGeocodeResponse(geocodeResponse);
    }

    public void isValidGeocodeResponse (GeocodeResponse geocodeResponse, String zipCodeOrCompleteAddress) {
        final String status = "Status: ";
        if (geocodeResponse.getStatus().equals("ZERO_RESULTS"))
            throw new ExternalResponseNotReceivedException(status.concat(geocodeResponse.getStatus()).concat(". Parameter passed: ").concat(zipCodeOrCompleteAddress));
        else if (geocodeResponse.getStatus().equals("REQUEST_DENIED"))
            throw new ExternalResponseNotReceivedException(status.concat(geocodeResponse.getStatus()).concat(". External error received: ").concat(geocodeResponse.getErrorMessage()).concat(" Parameter passed: ").concat(zipCodeOrCompleteAddress));
        else if (isDifferent("OK", geocodeResponse.getStatus()))
            throw new ExternalResponseNotReceivedException(status.concat(geocodeResponse.getStatus()).concat(". External error received: ").concat(geocodeResponse.getErrorMessage()));
    }

    /**
     * @param geocodeResponse eh um objeto mapeado com dados retornados do GoogleMapsAPI
     * @return AddressEnriched eh um objeto que possui o mapeamento de informacoes necessarias para relacionar endereco e usuario.
     */
    public AddressEnriched instantiateAddressEnrichedFromGeocodeResponse (GeocodeResponse geocodeResponse) {
        Address.AddressBuilder addressBuilder = Address.builder();
        AddressEnriched.AddressEnrichedBuilder addressEnrichedBuilder = AddressEnriched.builder();
        final String postalCodeKey = "postal_code";
        final String streetKey = "route";
        final String neighborhoodKey = "sublocality_level_1";
        final String cityKey = "administrative_area_level_2";
        final String stateKey = "administrative_area_level_1";
        final String countryKey = "country";
        final String streetNumberKey = "street_number";

        geocodeResponse.getResults().forEach(addressResult -> {
            addressResult.getAddressComponents().forEach(addressComponent -> {
                List<String> componentTypes = addressComponent.getTypes();
                if (componentTypes.contains(postalCodeKey))
                    addressBuilder.zipcode(removeNonNumeric(addressComponent.getLongName()));
                else if (componentTypes.contains(streetKey))
                    addressBuilder.street(addressComponent.getLongName());
                else if (componentTypes.contains(neighborhoodKey))
                    addressBuilder.neighborhood(addressComponent.getLongName());
                else if (componentTypes.contains(cityKey))
                    addressBuilder.city(addressComponent.getLongName());
                else if (componentTypes.contains(stateKey))
                    addressBuilder.state(addressComponent.getLongName());
                else if (componentTypes.contains(countryKey))
                    addressBuilder.country(addressComponent.getLongName());
                else if (componentTypes.contains(streetNumberKey))
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
