package com.naipy.alpha.modules.user_address.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.pk.UserAddressPK;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "tb_user_address")
@Getter
@Setter
@ToString
public class UserAddress implements Serializable {

    @Serial
    private static final long serialVersionUID = UniversalSerialVersion.USER_ADDRESS_SERIAL_VERSION_UID;

    @EmbeddedId
    private UserAddressPK id = new UserAddressPK();

    @NotBlank
    private String complement;

    @NotBlank
    private String streetNumber;

    @NotBlank
    private String latitude;

    @NotBlank
    private String longitude;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AddressUsageType usageType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant insertDate;

    public UserAddress() {
    }

    public UserAddress(User user, Address address, String complement, String streetNumber, String latitude, String longitude, AddressUsageType usageType) {
        setUser(user);
        setAddress(address);
        this.complement = complement;
        this.streetNumber = streetNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.usageType = usageType;
        this.insertDate = Instant.now();
    }

    @JsonIgnore
    public User getUser () {
        return getId().getUser();
    }

    public void setUser (User user) {
        getId().setUser(user);
    }

    public Address getAddress () {
        return getId().getAddress();
    }

    public void setAddress (Address address) {
        getId().setAddress(address);
    }
}
