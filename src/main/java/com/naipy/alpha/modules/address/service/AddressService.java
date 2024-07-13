package com.naipy.alpha.modules.address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.city.models.City;
import com.naipy.alpha.modules.city.repository.CityRepository;
import com.naipy.alpha.modules.country.models.Country;
import com.naipy.alpha.modules.country.repository.CountryRepository;
import com.naipy.alpha.modules.exceptions.services.ExternalResponseNotReceivedException;
import com.naipy.alpha.modules.state.models.State;
import com.naipy.alpha.modules.state.repository.StateRepository;
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
    CountryRepository _countryRepository;
    @Autowired
    StateRepository _stateRepository;
    @Autowired
    CityRepository _cityRepository;
    @Autowired
    AddressRepository _addressRepository;
    @Autowired
    MapsService _mapsService;


    /**
     * Primeiro, consultar no Redis. Caso nao exista, consultar no Postgrees. Caso nao exista, consultar na API GoogleMaps e salvar nos bancos.
     * @param zipCode eh um codigo postal/CEP
     * @return Address - Retorna um endereco salvo no banco
     */
    public Address getAddressAndAddIfDoesntExists (String zipCode) {
        zipCode = removeNonNumeric(zipCode);
        Optional<Address> optionalAddress = _addressRepository.findAddressByZipCode(zipCode);
        if (optionalAddress.isEmpty()) {
            GeocodeResponse geocodeResponse =
                    _mapsService.getAddressByZipCodeOrCompleteAddressFromMapsApi(zipCode);
            isValidGeocodeResponse(geocodeResponse, zipCode);
            return _addressRepository.save(instantiateAddressEnrichedFromGeocodeResponse(geocodeResponse).getAddress());
        }
        else
            return optionalAddress.get();
    }

    public AddressEnriched getAddressEnrichedByCompleteAddress (String completeAddress) {
        GeocodeResponse geocodeResponse = _mapsService.getAddressByZipCodeOrCompleteAddressFromMapsApi(completeAddress);
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
        City city = new City();
        State state = new State();
        Country country = new Country();

        geocodeResponse.getResults().forEach(addressResult -> {
            addressResult.getAddressComponents().forEach(addressComponent -> {
                List<String> componentTypes = addressComponent.getTypes();
                if (componentTypes.contains("postal_code")) {
                    addressBuilder.zipcode(addressComponent.getLongName());
                }
                else if (componentTypes.contains("route"))
                    addressBuilder.street(addressComponent.getLongName());
                else if (componentTypes.contains("sublocality_level_1"))
                    addressBuilder.neighborhood(addressComponent.getLongName());
                else if (componentTypes.contains("administrative_area_level_2")) {
                    city.setId(generateUUID());
                    city.setName(addressComponent.getLongName());
                    city.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("administrative_area_level_1")) {
                    state.setId(generateUUID());
                    state.setName(addressComponent.getLongName());
                    state.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("country")) {
                    country.setId(generateUUID());
                    country.setName(addressComponent.getLongName());
                    country.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("street_number"))
                    addressEnrichedBuilder.streetNumber(addressComponent.getLongName());
            });
            addressBuilder.latitude(addressResult.getGeometry().getLocation().getLat());
            addressBuilder.longitude(addressResult.getGeometry().getLocation().getLng());
        });
        Optional<Country> countryAlreadyExists = _countryRepository.findByName(country.getName());
        Country savedCountry = countryAlreadyExists.orElseGet(() -> _countryRepository.save(country));

        state.setCountry(savedCountry);
        Optional<State> stateAlreadyExists = _stateRepository.findByName(state.getName());
        State savedState = stateAlreadyExists.orElseGet(() -> _stateRepository.save(state));

        city.setState(savedState);
        Optional<City> cityAlreadyExists = _cityRepository.findByName(city.getName());
        City savedCity = cityAlreadyExists.orElseGet(() -> _cityRepository.save(city));

        //Nao eh necessário fazer validação do zipCode porque, no metodo onde invoca esse método, ja eh feita a busca por zipcode se existe algum endereço cadastrado
        addressBuilder.id(generateUUID());
        addressBuilder.city(savedCity);

        addressEnrichedBuilder.address(addressBuilder.build());
        return addressEnrichedBuilder.build();
    }
}
