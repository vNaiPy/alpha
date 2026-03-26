package com.naipy.alpha.modules.user_address.service.impl;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.address.service.AddressService;
import com.naipy.alpha.modules.exceptions.services.InvalidParameterException;
import com.naipy.alpha.modules.user.controllers.AddressInput;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.repository.UserAddressRepository;
import com.naipy.alpha.modules.user_address.service.UserAddressService;
import com.naipy.alpha.modules.utils.ConstantVariables;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    private static final Logger logger = LoggerFactory.getLogger(UserAddressServiceImpl.class);

    private final AddressService addressService;

    private final UserAddressRepository userAddressRepository;

    @Autowired
    public UserAddressServiceImpl(AddressService addressService, UserAddressRepository userAddressRepository) {
        this.addressService = addressService;
        this.userAddressRepository = userAddressRepository;
    }

    public UserAddress addAddressToUser (AddressInput addressInput) {
        AddressDTO addressDTO = addressService.getAddressAndAddIfDoesntExists(addressInput.zipCode());
        Address address = getExactAddressOfUser(addressDTO, addressInput.streetNumber());

        User currentUser = ServiceUtils.getCurrentUser();
        UserAddress userAddress = new UserAddress();
        userAddress.setUser(currentUser);
        userAddress.setAddress(address);
        userAddress.setLongitude(address.getLongitude());
        userAddress.setLatitude(address.getLatitude());
        userAddress.setStreetNumber(address.getStreetNumber());
        userAddress.setComplement(addressInput.complement());
        userAddress.setUsageType(addressInput.addressUsageType());

        return userAddressRepository.save(userAddress);
    }

    public Address getExactAddressOfUser (AddressDTO address, String streetNumber) {
        String addressComplete = address.getStreet().concat(ConstantVariables.WHITESPACE)
                .concat(streetNumber).concat(ConstantVariables.WHITESPACE)
                .concat(address.getNeighborhood()).concat(ConstantVariables.WHITESPACE)
                .concat(address.getCity()).concat(ConstantVariables.WHITESPACE)
                .concat(address.getState()).concat(ConstantVariables.WHITESPACE)
                .concat(address.getCountry());

        if (addressComplete.isBlank()) {
            final String errorMessage = "Parameter is not valid for searching in the MapsAPI. Param: ".concat(addressComplete);
            logger.warn(errorMessage);
            throw new InvalidParameterException(errorMessage);
        }

        return addressService.getAddressByCompleteAddress(addressComplete);
    }
}
