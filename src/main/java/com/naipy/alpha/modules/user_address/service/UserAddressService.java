package com.naipy.alpha.modules.user_address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.service.AddressService;
import com.naipy.alpha.modules.exceptions.services.InvalidParameterException;
import com.naipy.alpha.modules.user.controllers.AddressInput;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.repository.UserAddressRepository;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAddressService extends ServiceUtils {

    @Autowired
    private AddressService _addressService;

    @Autowired
    private UserAddressRepository _userAddressRepository;

    public UserAddress addAddressToUser (AddressInput addressInput) {
        AddressDTO addressDTO = _addressService.getAddressAndAddIfDoesntExists(addressInput.zipCode());
        AddressEnriched addressEnriched = getExactAddressOfUser(addressDTO, addressInput.streetNumber());

        User currentUser = getIdCurrentUser();
        UserAddress userAddress = new UserAddress();
        userAddress.setUser(currentUser);
        userAddress.setAddress(addressEnriched.getAddress());
        userAddress.setLongitude(addressEnriched.getAddress().getLongitude());
        userAddress.setLatitude(addressEnriched.getAddress().getLatitude());
        userAddress.setStreetNumber(addressEnriched.getStreetNumber());
        userAddress.setComplement(addressInput.complement());
        userAddress.setUsageType(AddressUsageType.PERSONAL);
        userAddress.setIsDefault(true);
        return _userAddressRepository.save(userAddress);
    }

    public AddressEnriched getExactAddressOfUser (AddressDTO address, String streetNumber) {
        String addressComplete = address.getStreet() + WHITESPACE
                + streetNumber + WHITESPACE
                + address.getNeighborhood() + WHITESPACE
                + address.getCity() + WHITESPACE
                + address.getState() + WHITESPACE
                + address.getCountry();

        if (addressComplete.isBlank())
            throw new InvalidParameterException(addressComplete + ". This is not valid for searching in the MapsAPI");

        return _addressService.getAddressEnrichedByCompleteAddress(addressComplete);
    }
}
