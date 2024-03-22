package com.naipy.alpha.modules.utils.maps;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
