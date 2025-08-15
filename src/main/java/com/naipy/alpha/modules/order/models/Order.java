package com.naipy.alpha.modules.order.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.naipy.alpha.modules.order_item.model.OrderItem;
import com.naipy.alpha.modules.order.enums.OrderStatus;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_order")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = UniversalSerialVersion.ORDER_SERIAL_VERSION_UID;

    @Id
    @Column(unique = true, nullable = false)
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant moment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    public Double getTotal () {
        double sum = 0d;
        for(OrderItem orderItem : items) {
            sum += orderItem.getSubTotal();
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
