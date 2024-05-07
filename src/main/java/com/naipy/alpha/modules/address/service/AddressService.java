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
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.utils.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.utils.maps.services.MapsService;
import com.naipy.alpha.modules.zipcode.models.ZipCode;
import com.naipy.alpha.modules.zipcode.repository.ZipCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Address addIfDoesntExistsAndGetAddress (String zipCode) {
        Optional<Address> optionalAddress = _addressRepository.findAddressByZipCode(zipCode);

        if (optionalAddress.isEmpty()) {
            GeocodeResponse geocodeResponse = _mapsService.getAddressBySomethingFromMapsApi(zipCode);
            return _addressRepository.save(instantiateAddressFromGeocodeResponse(geocodeResponse).getAddress());
        }
        else
            return optionalAddress.get();
    }

    public UserAddress getAddressToUser (String addressComplete) {
        GeocodeResponse geocodeResponse = _mapsService.getAddressBySomethingFromMapsApi(addressComplete);


        return null;
    }

    /**
     * @param geocodeResponse
     * @return UserAddress
     * @implNote Recebe um objeto de resposta do maps api, salva algumas entidades no banco relacionado ao endereço e retorna uma instância de UserAddress
     */
    public UserAddress instantiateAddressFromGeocodeResponse (GeocodeResponse geocodeResponse) {
        UserAddress userAddress = new UserAddress();
        Address.AddressBuilder addressBuilder = Address.builder();
        ZipCode zipCode = new ZipCode();
        City city = new City();
        State state = new State();
        Country country = new Country();
        Coordinate coordinate = new Coordinate();

        geocodeResponse.getResults().forEach(addressResult -> {
            addressResult.getAddressComponents().forEach(addressComponent -> {
                if (addressComponent.getTypes().contains("postal_code")) {
                    zipCode.setId(ServiceUtils.generateUUID());
                    zipCode.setCode(addressComponent.getLongName());
                }
                else if (addressComponent.getTypes().contains("route"))
                    addressBuilder.street(addressComponent.getLongName());
                else if (addressComponent.getTypes().contains("sublocality_level_1"))
                    addressBuilder.neighborhood(addressComponent.getLongName());
                else if (addressComponent.getTypes().contains("administrative_area_level_2")) {
                    city.setId(ServiceUtils.generateUUID());
                    city.setName(addressComponent.getLongName());
                    city.setCode(addressComponent.getShortName());
                }
                else if (addressComponent.getTypes().contains("administrative_area_level_1")) {
                    state.setId(ServiceUtils.generateUUID());
                    state.setName(addressComponent.getLongName());
                    state.setCode(addressComponent.getShortName());
                }
                else if (addressComponent.getTypes().contains("country")) {
                    country.setId(ServiceUtils.generateUUID());
                    country.setName(addressComponent.getLongName());
                    country.setCode(addressComponent.getShortName());
                }
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
