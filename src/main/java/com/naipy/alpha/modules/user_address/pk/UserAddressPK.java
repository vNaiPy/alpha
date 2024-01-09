package com.naipy.alpha.modules.user_address.pk;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.user.models.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UserAddressPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
