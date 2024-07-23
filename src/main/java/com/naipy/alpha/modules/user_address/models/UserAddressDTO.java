package com.naipy.alpha.modules.user_address.models;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressDTO;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.models.UserDTO;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class UserAddressDTO {

    private final UserDTO userDTO;
    private final AddressDTO addressDTO;
    private final String complement;
    private final String streetNumber;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final AddressUsageType usageType;

    public UserAddressDTO (UserAddress userAddress) {
        this.userDTO = UserDTO.createUserDTO(userAddress.getUser());
        this.addressDTO = new AddressDTO(userAddress.getAddress());
        this.complement = userAddress.getComplement();
        this.streetNumber = userAddress.getStreetNumber();
        this.latitude = userAddress.getLatitude();
        this.longitude = userAddress.getLongitude();
        this.usageType = userAddress.getUsageType();
    }
}
