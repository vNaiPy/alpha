package com.naipy.alpha.modules.zipcode.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.address.models.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_zipcodes")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZipCode {

    @Id
    private UUID id;

    @NotNull
    @Column(length = 12)
    private String code;

    @JsonIgnore
    @OneToOne(mappedBy = "zipCode", cascade = CascadeType.ALL)
    private Address address;
}
