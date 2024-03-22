package com.naipy.alpha.modules.coords.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_coordinates")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate implements Serializable {

    @Id
    private UUID id;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @JsonIgnore
    @OneToMany(mappedBy = "coordinate")
    private Set<UserAddress> userAddress = new HashSet<>();
}
