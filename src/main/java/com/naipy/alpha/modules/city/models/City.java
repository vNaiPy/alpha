package com.naipy.alpha.modules.city.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.state.models.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_cities")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City implements Serializable {

    @Id
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @JsonIgnore
    @OneToMany(mappedBy = "city")
    private List<Address> address = new ArrayList<>();

}
