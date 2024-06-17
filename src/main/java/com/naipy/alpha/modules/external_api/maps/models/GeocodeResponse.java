package com.naipy.alpha.modules.external_api.maps.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GeocodeResponse {

    @JsonProperty("error_message")
    private String errorMessage;
    private String status;
    private List<AddressResult> results = new ArrayList<>();

}
