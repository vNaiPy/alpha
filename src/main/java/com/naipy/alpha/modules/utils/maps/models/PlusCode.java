package com.naipy.alpha.modules.utils.maps.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlusCode {

    @JsonProperty("compound_code")
    private String compoundCode;
    @JsonProperty("global_code")
    private String globalCode;

}
