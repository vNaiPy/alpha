package com.naipy.alpha.modules.state.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.city.models.City;
import com.naipy.alpha.modules.country.models.Country;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_states")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class State implements Serializable {

    @Id
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @JsonIgnore
    @OneToMany(mappedBy = "state")
    private List<City> city = new ArrayList<>();

}
