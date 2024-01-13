package com.naipy.alpha.modules.user_address.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.coords.models.Coordinate;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.pk.UserAddressPK;
import com.naipy.alpha.modules.user.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

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
    private Boolean isDefault;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AddressUsageType usageType;

    @ManyToOne
    @JoinColumn(name = "coordinate_id")
    private Coordinate coordinate;

    public UserAddress() {
    }

    public UserAddress(User user, Address address, String complement, String streetNumber, Boolean isDefault, AddressUsageType usageType, Coordinate coordinate) {
        setUser(user);
        setAddress(address);
        this.complement = complement;
        this.streetNumber = streetNumber;
        this.isDefault = isDefault;
        this.usageType = usageType;
        this.coordinate = coordinate;
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
