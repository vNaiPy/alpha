package com.naipy.alpha.modules.utils.maps;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressResult {

    private String place_id;
    private String formatted_address;
    private Geometry geometry;
    private PlusCode plus_code;
    private List<AddressComponent> address_components = new ArrayList<>();
    private List<String> postcode_localities = new ArrayList<>();
    private List<String> types = new ArrayList<>();

}
