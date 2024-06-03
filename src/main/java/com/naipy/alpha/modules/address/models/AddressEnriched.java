package com.naipy.alpha.modules.address.models;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressEnriched {

    private Address address;

    private String streetNumber;
}
