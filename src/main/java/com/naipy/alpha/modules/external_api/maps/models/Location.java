package com.naipy.alpha.modules.external_api.maps.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Location {
    private String lat;
    private String lng;
}
