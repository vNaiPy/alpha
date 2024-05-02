package com.naipy.alpha.modules.utils.maps.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Viewport {

    private Location northeast;
    private Location southwest;

}
