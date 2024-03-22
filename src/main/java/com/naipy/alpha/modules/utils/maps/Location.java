package com.naipy.alpha.modules.utils.maps;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Location {
    private BigDecimal lat;
    private BigDecimal lng;
}
