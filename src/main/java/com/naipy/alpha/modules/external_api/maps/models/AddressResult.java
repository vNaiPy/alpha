package com.naipy.alpha.modules.external_api.maps.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressResult {

    @JsonProperty("place_id")
    private String placeId;
    @JsonProperty("formatted_address")
    private String formattedAddress;
    private Geometry geometry;
    @JsonProperty("plus_code")
    private PlusCode plusCode;
    @JsonProperty("address_components")
    private List<AddressComponent> addressComponents = new ArrayList<>();
    @JsonProperty("postcode_localities")
    private List<String> postcodeLocalities = new ArrayList<>();
    private List<String> types = new ArrayList<>();

}
