package com.naipy.alpha.modules.external_api.maps.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Geometry {

    private Location location;
    @JsonProperty("location_type")
    private String locationType;
    private Viewport viewport;
    private Bounds bounds;

}
