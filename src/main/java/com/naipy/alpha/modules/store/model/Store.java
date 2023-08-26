package com.naipy.alpha.modules.store.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.localization.model.Localization;
import com.naipy.alpha.modules.user.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tb_store")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String logoUrl;
    private String bannerUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant instant;

    @ManyToMany
    @JoinTable(name = "tb_localization_store",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "localization_id"))
    private List<Localization> addresses;

    @JsonIgnore
    @OneToOne
    @MapsId
    private User owner;
}