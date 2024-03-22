package com.naipy.alpha.modules.utils.maps;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Geometry {

    private Location location;
    private String location_type;
    private Viewport viewport;
    private Bounds bounds;

}
