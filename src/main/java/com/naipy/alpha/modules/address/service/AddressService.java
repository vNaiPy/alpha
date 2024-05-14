package com.naipy.alpha.modules.address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.city.models.City;
import com.naipy.alpha.modules.city.repository.CityRepository;
import com.naipy.alpha.modules.coords.models.Coordinate;
import com.naipy.alpha.modules.country.models.Country;
import com.naipy.alpha.modules.country.repository.CountryRepository;
import com.naipy.alpha.modules.state.models.State;
import com.naipy.alpha.modules.state.repository.StateRepository;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.utils.maps.models.AddressComponent;
import com.naipy.alpha.modules.utils.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.utils.maps.services.MapsService;
import com.naipy.alpha.modules.zipcode.models.ZipCode;
import com.naipy.alpha.modules.zipcode.repository.ZipCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 */
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


    /**
     * Primeiro, consultar no Redis. Caso nao exista, consultar no Postgrees. Caso nao exista, consultar na API GoogleMaps e salvar nos bancos.
     * @param zipCode recebe um CEP
     * @return Address - Retorna um endereço salvo no banco
     */
    public Address addIfDoesntExistsAndGetAddress (String zipCode) {
        Optional<Address> optionalAddress = _addressRepository.findAddressByZipCode(zipCode);

        if (optionalAddress.isEmpty()) {
            GeocodeResponse geocodeResponse = _mapsService.getAddressByZipCodeOrCompleteAddressFromMapsApi(zipCode);
            return _addressRepository.save(instantiateUserAddressFromGeocodeResponse(geocodeResponse).getAddress());
        }
        else
            return optionalAddress.get();
    }

    public UserAddress getUserAddressToUser (String addressComplete) {
        GeocodeResponse geocodeResponse = _mapsService.getAddressByZipCodeOrCompleteAddressFromMapsApi(addressComplete);
        return instantiateUserAddressFromGeocodeResponse(geocodeResponse);
    }

    /**
     * @param geocodeResponse eh um objeto mapeado com dados retornados do GoogleMapsAPI
     * @return UserAddress eh um objeto que possui o mapeamento de informações necessárias para relacionar endereço e usuário.
     */
    public UserAddress instantiateUserAddressFromGeocodeResponse (GeocodeResponse geocodeResponse) {
        UserAddress userAddress = new UserAddress();
        Address.AddressBuilder addressBuilder = Address.builder();
        ZipCode zipCode = new ZipCode();
        City city = new City();
        State state = new State();
        Country country = new Country();
        Coordinate coordinate = new Coordinate();

        geocodeResponse.getResults().forEach(addressResult -> {
            addressResult.getAddressComponents().forEach(addressComponent -> {
                List<String> componentTypes = addressComponent.getTypes();
                if (componentTypes.contains("postal_code")) {
                    zipCode.setId(ServiceUtils.generateUUID());
                    zipCode.setCode(addressComponent.getLongName());
                }
                else if (componentTypes.contains("route"))
                    addressBuilder.street(addressComponent.getLongName());
                else if (componentTypes.contains("sublocality_level_1"))
                    addressBuilder.neighborhood(addressComponent.getLongName());
                else if (componentTypes.contains("administrative_area_level_2")) {
                    city.setId(ServiceUtils.generateUUID());
                    city.setName(addressComponent.getLongName());
                    city.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("administrative_area_level_1")) {
                    state.setId(ServiceUtils.generateUUID());
                    state.setName(addressComponent.getLongName());
                    state.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("country")) {
                    country.setId(ServiceUtils.generateUUID());
                    country.setName(addressComponent.getLongName());
                    country.setCode(addressComponent.getShortName());
                }
                else if (componentTypes.contains("street_number"))
                    userAddress.setStreetNumber(addressComponent.getLongName());
            });
            coordinate.setLatitude(addressResult.getGeometry().getLocation().getLat());
            coordinate.setLongitude(addressResult.getGeometry().getLocation().getLng());
        });
        Optional<Country> countryAlreadyExists = _countryRepository.findByName(country.getName());
        Country savedCountry = countryAlreadyExists.orElseGet(() -> _countryRepository.save(country));

        state.setCountry(savedCountry);
        Optional<State> stateAlreadyExists = _stateRepository.findByName(state.getName());
        State savedState = stateAlreadyExists.orElseGet(() -> _stateRepository.save(state));

        city.setState(savedState);
        Optional<City> cityAlreadyExists = _cityRepository.findByName(city.getName());
        City savedCity = cityAlreadyExists.orElseGet(() -> _cityRepository.save(city));

        //Não é necessário fazer validação do zipCode porque, no método onde invoca esse método, já é feita a busca por zipcode se existe algum endereço cadastrado
        ZipCode savedZipCode = _zipCodeRepository.save(zipCode);
        addressBuilder.id(ServiceUtils.generateUUID());
        addressBuilder.city(savedCity);
        addressBuilder.zipCode(savedZipCode);

        coordinate.setId(ServiceUtils.generateUUID());
        userAddress.setCoordinate(coordinate);
        userAddress.setAddress(addressBuilder.build());
        return userAddress;
    }
}
