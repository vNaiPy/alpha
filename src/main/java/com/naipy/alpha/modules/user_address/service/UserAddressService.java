package com.naipy.alpha.modules.user_address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.address.service.AddressService;
import com.naipy.alpha.modules.exceptions.services.InvalidParameterException;
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

    public UserAddress addAddressToUser (String zipCode, String streetNumber, String complement) {
        Address address = _addressService.getAddressAndAddIfDoesntExists(zipCode);
        String addressComplete = address.getStreet()
                + streetNumber
                + address.getNeighborhood()
                + address.getCity().getName()
                + address.getCity().getState().getName()
                + address.getCity().getState().getCountry().getName();

        if (addressComplete.isEmpty())
            throw new InvalidParameterException(addressComplete + ". This is not valid for searching in the MapsAPI");
        User currentUser = getIdCurrentUser();
        UserAddress userAddress = new UserAddress();
        AddressEnriched addressEnriched = _addressService.getAddressEnrichedByCompleteAddress(addressComplete);
        userAddress.setUser(currentUser);
        userAddress.setAddress(addressEnriched.getAddress());
        userAddress.setLongitude(addressEnriched.getAddress().getLongitude());
        userAddress.setLatitude(addressEnriched.getAddress().getLatitude());
        userAddress.setStreetNumber(addressEnriched.getStreetNumber());
        userAddress.setComplement(complement);
        userAddress.setUsageType(AddressUsageType.PERSONAL);
        userAddress.setIsDefault(true);
        return _userAddressRepository.save(userAddress);
    }
}
