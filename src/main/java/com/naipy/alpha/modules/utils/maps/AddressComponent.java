package com.naipy.alpha.modules.utils.maps;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressComponent {

    private String long_name;
    private String short_name;
    private List<String> types = new ArrayList<>();

}
