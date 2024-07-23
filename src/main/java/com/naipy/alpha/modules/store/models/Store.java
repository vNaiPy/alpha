package com.naipy.alpha.modules.store.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.models.Localization;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_store")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store implements Serializable {

    @Id
    private UUID id;
    private String name;
    private String logoUrl;
    private String bannerUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant instant;

    @OneToOne
    private Localization address;

    @JsonIgnore
    @OneToOne
    @MapsId
    private User owner;
}