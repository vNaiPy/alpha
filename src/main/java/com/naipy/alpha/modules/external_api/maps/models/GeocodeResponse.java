package com.naipy.alpha.modules.external_api.maps.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.naipy.alpha.modules.exceptions.services.ExternalResponseNotReceivedException;
import com.naipy.alpha.modules.utils.ServiceUtils;
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

    public static void isValidGeocodeResponse (GeocodeResponse geocodeResponse, String zipCodeOrCompleteAddress) {
        final String status = "Code received from external API. Status: ";
        if (geocodeResponse.getStatus().equals("ZERO_RESULTS"))
            throw new ExternalResponseNotReceivedException(status.concat(geocodeResponse.getStatus()).concat(". Parameter passed: ").concat(zipCodeOrCompleteAddress));
        else if (geocodeResponse.getStatus().equals("REQUEST_DENIED"))
            throw new ExternalResponseNotReceivedException(status.concat(geocodeResponse.getStatus()).concat(". External error received: ").concat(geocodeResponse.getErrorMessage()).concat(" Parameter passed: ").concat(zipCodeOrCompleteAddress));
        else if (ServiceUtils.isDifferent("OK", geocodeResponse.getStatus()))
            throw new ExternalResponseNotReceivedException(status.concat(geocodeResponse.getStatus()).concat(". External error received: ").concat(geocodeResponse.getErrorMessage()));
    }

}
