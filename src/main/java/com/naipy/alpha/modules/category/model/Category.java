package com.naipy.alpha.modules.category.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_category")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = UniversalSerialVersion.CATEGORY_SERIAL_VERSION_UID;

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private String imgUrl;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String subname;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();
}
