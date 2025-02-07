package com.naipy.alpha.modules.order_item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.order_item.pk.OrderItemPK;
import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.product.model.Product;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_order_item")
@Data
public class OrderItem implements Serializable {

    @EmbeddedId
    private OrderItemPK id = new OrderItemPK();
    private Integer quantity;
    private Double price;

    public OrderItem() {
    }

    public OrderItem(Order order, Product product, Integer quantity, Double price) {
        id.setOrder(order);
        id.setProduct(product);
        this.quantity = quantity;
        this.price = price;
    }

    @JsonIgnore
    public Order getOrder () {
        return id.getOrder();
    }

    public void setOrder (Order order) {
        id.setOrder(order);
    }

    public Product getProduct () {
        return id.getProduct();
    }

    public void setProduct (Product product) {
        id.setProduct(product);
    }

    public Double getSubTotal () {
        return price * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
