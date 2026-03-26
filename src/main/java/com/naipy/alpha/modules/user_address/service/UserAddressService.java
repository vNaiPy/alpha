package com.naipy.alpha.modules.user_address.service;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.user.controllers.AddressInput;
import com.naipy.alpha.modules.user_address.models.UserAddress;

public interface UserAddressService {

    UserAddress addAddressToUser (AddressInput addressInput);
    Address getExactAddressOfUser (AddressDTO address, String streetNumber);

}
