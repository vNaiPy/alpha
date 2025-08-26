package com.naipy.alpha.modules.order_item.pk;

import com.naipy.alpha.modules.order.models.Order;
import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode
public class OrderItemPK implements Serializable {

    @Serial
    private static final long serialVersionUID = UniversalSerialVersion.ORDER_PRODUCT_PK_SERIAL_VERSION_UID;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
