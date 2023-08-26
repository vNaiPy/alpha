package com.naipy.alpha.modules.localization.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.store.model.Store;
import com.naipy.alpha.modules.user.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
