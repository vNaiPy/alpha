package com.naipy.alpha.modules.zipcode.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.address.models.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tb_zipcodes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZipCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(length = 12)
    private String zipCode;

    @JsonIgnore
    @OneToOne(mappedBy = "zipCode", cascade = CascadeType.ALL)
    private Address address;

}
