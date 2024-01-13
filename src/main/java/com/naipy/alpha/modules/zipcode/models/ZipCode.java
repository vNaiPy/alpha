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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(length = 12)
    private String zipCode;

    @JsonIgnore
    @OneToOne(mappedBy = "zipCode", cascade = CascadeType.ALL)
    private Address address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZipCode zipCode = (ZipCode) o;
        return Objects.equals(id, zipCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
