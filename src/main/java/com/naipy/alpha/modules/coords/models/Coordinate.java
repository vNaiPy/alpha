package com.naipy.alpha.modules.coords.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
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
@ToString
public class Coordinate implements Serializable {

    @Id
    private UUID id;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @JsonIgnore
    @OneToMany(mappedBy = "coordinate")
    @ToString.Exclude
    private Set<UserAddress> userAddress = new HashSet<>();
}
