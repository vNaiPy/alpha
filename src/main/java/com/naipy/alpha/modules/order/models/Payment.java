package com.naipy.alpha.modules.order.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_payment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Payment implements Serializable {

    @Id
    private Long id;
    private Instant instant;

    @JsonIgnore
    @OneToOne
    @MapsId
    private Order order;
}
