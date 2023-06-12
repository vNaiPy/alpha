package com.naipy.alpha.modules.order_item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.order_item.model.OrderItem;
import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.order_item.pk.OrderItemPK;
import com.naipy.alpha.modules.product.model.ProductDTO;


public class OrderItemDTO {

    private OrderItemPK id = new OrderItemPK();
    private Integer quantity;
    private Double price;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItem orderItem) {
        id.setOrder(orderItem.getOrder());
        id.setProduct(orderItem.getProduct());
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }

    @JsonIgnore
    public Order getOrder () {
        return id.getOrder();
    }

    public void setOrder (Order order) {
        id.setOrder(order);
    }

    public ProductDTO getProduct () {
        return ProductDTO.createProductDTO(id.getProduct());
    }

    public void setProduct (Product product) {
        id.setProduct(product);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubTotal () {
        return price * quantity;
    }
}
