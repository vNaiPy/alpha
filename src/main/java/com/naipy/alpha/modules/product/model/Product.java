package com.naipy.alpha.modules.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.order_item.model.OrderItem;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_products")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = UniversalSerialVersion.PRODUCT_SERIAL_VERSION_UID;

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull
    @Column(nullable = false)
    private Double price;

    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(name = "tb_product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "id.product")
    private Set<OrderItem> items = new HashSet<>();

    @JsonIgnore
    public Set<Order> getOrders () {
        Set<Order> orderSet = new HashSet<>();
        for(OrderItem oi : items) {
            orderSet.add(oi.getOrder());
        }
        return orderSet;
    }

}