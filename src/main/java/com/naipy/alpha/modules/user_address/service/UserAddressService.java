package com.naipy.alpha.modules.user_address.service;

import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.service.AddressService;
import com.naipy.alpha.modules.exceptions.services.InvalidParameterException;
import com.naipy.alpha.modules.user.controllers.AddressInput;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.repository.UserAddressRepository;
import com.naipy.alpha.modules.utils.ConstantVariables;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAddressService extends ServiceUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserAddressService.class);

    private final AddressService addressService;

    private final UserAddressRepository userAddressRepository;

    @Autowired
    public UserAddressService(AddressService addressService, UserAddressRepository userAddressRepository) {
        this.addressService = addressService;
        this.userAddressRepository = userAddressRepository;
    }

    public UserAddress addAddressToUser (AddressInput addressInput, AddressUsageType addressUsageType) {
        AddressDTO addressDTO = addressService.getAddressAndAddIfDoesntExists(addressInput.zipCode());
        AddressEnriched addressEnriched = getExactAddressOfUser(addressDTO, addressInput.streetNumber());

        User currentUser = getIdCurrentUser();
        UserAddress userAddress = new UserAddress();
        userAddress.setUser(currentUser);
        userAddress.setAddress(addressEnriched.getAddress());
        userAddress.setLongitude(addressEnriched.getAddress().getLongitude());
        userAddress.setLatitude(addressEnriched.getAddress().getLatitude());
        userAddress.setStreetNumber(addressEnriched.getStreetNumber());
        userAddress.setComplement(addressInput.complement());
        userAddress.setUsageType(addressUsageType);

        return userAddressRepository.save(userAddress);
    }

    public AddressEnriched getExactAddressOfUser (AddressDTO address, String streetNumber) {
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

        return addressService.getAddressEnrichedByCompleteAddress(addressComplete);
    }
}
