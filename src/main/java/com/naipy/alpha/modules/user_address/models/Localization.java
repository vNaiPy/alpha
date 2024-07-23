package com.naipy.alpha.modules.user_address.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.store.models.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_localization")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Localization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String country;
    private Double longitude;
    private Double latitude;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private Store store;
}
