package com.naipy.alpha.modules.store.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.store.enums.StoreStatus;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

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
    @Column(nullable = false, unique = true)
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private String logoUrl;

    private String bannerUrl;

    @Column(length = 500)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    @NotNull
    @Column(nullable = false)
    private Instant createdAt;

    @NotBlank
    @Column(nullable = false)
    private StoreStatus storeStatus;

    @JsonIgnore
    @OneToOne
    @MapsId
    private User owner;
}