package com.naipy.alpha.modules.order.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "tb_payment")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = UniversalSerialVersion.PAYMENT_SERIAL_VERSION_UID;

    @Id
    private String id;
    private Instant createdAt;

    @JsonIgnore
    @OneToOne
    @MapsId
    private Order order;
}
