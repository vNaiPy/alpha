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
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    @Column(length = 3)
    private String code;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<State> state = new ArrayList<>();
}
