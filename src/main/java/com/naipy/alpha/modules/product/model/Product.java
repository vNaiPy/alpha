package com.naipy.alpha.modules.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.order_item.model.OrderItem;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.category.model.Category;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_products")
public class Product implements Serializable {
    @Id
    private UUID id;
    private String name;
    private String description;
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

    public Product() {
    }

    public Product(UUID id, String name, String description, Double price, String imgUrl, ProductStatus status, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.status = status;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    @JsonIgnore
    public Set<Order> getOrders () {
        Set<Order> orderSet = new HashSet<>();
        for(OrderItem oi : items) {
            orderSet.add(oi.getOrder());
        }
        return orderSet;
    }

}