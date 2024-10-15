package com.naipy.alpha.modules.utils;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;

public class ChargeObject extends ServiceUtils {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User getOneUser () {
        return User.builder()
                .id(ServiceUtils.generateUUID())
                .name("Bruna")
                .surname("Meyer")
                .email("bruna@mail.com")
                .phone("119999999999")
                .password(passwrdEncoder("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .profilePicture("https://")
                .registeredSince(Instant.now())
                .lastUpdate(Instant.now())
                .build();
    }

    public static String passwrdEncoder (String password) {
        return passwordEncoder.encode(password);
    }

    public static UserAddress getOneUserAddress () {
        return new UserAddress(
                getOneUser()
                , ChargeAddressObject.getOneAddress()
                , "Condominio San Diego"
                , "130"
                , "-23.6509129"
                , "-46.57409550000001",
                true,
                AddressUsageType.PERSONAL);
    }
}
