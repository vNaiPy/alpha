package com.naipy.alpha.modules.user_address.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.pk.UserAddressPK;
import com.naipy.alpha.modules.user.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_user_address")
@Getter
@Setter
@ToString
public class UserAddress implements Serializable {

    @EmbeddedId
    private UserAddressPK id = new UserAddressPK();

    @NotNull
    private String complement;

    @NotNull
    private String streetNumber;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    private Boolean isDefault;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AddressUsageType usageType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant insertDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant lastUpdate;

    public UserAddress() {
    }

    public UserAddress(User user, Address address, String complement, String streetNumber, BigDecimal latitude, BigDecimal longitude, Boolean isDefault, AddressUsageType usageType) {
        setUser(user);
        setAddress(address);
        this.complement = complement;
        this.streetNumber = streetNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isDefault = isDefault;
        this.usageType = usageType;
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
