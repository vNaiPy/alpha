package com.naipy.alpha.modules.country.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.state.models.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_countries")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Country implements Serializable {

    @Id
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    @ToString.Exclude
    private List<State> state = new ArrayList<>();
}
