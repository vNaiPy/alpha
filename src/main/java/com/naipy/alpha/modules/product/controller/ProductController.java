package com.naipy.alpha.modules.product.controller;

import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService _productService;

    @QueryMapping
    public List<ProductDTO> findAllProducts () {
        return _productService.findAll();
    }

    @QueryMapping
    public ProductDTO findByProductId (@Argument Long id) {
        return _productService.findById(id);
    }

    @QueryMapping
    public List<ProductDTO> searchingForWithLngLat (@Argument String searchingFor, @Argument Double lng, @Argument Double lat) {
        return _productService.searchingForWithLngLat(searchingFor, lng, lat);
    };

    @QueryMapping
    public List<ProductDTO> findAllProductsByOwnerId (@Argument Long id) {
        return _productService.findAllByOwner(id);
    }

    @MutationMapping
    @Secured("USER")
    public ProductDTO addProduct (@Argument ProductInput product) {
        Product newProduct = new Product();
        newProduct.setName(product.name);
        newProduct.setDescription(product.description);
        newProduct.setPrice(product.price);
        newProduct.setImgUrl(product.imgUrl);

        Set<Category> categoryIdSet = product.categoryIdList().stream()
                .map(categoryId -> Category.builder().id(categoryId).build())
                .collect(Collectors.toSet());
        return _productService.insert(newProduct, categoryIdSet);
    }

    @MutationMapping
    @Secured("USER")
    public ProductDTO updateProduct (@Argument Long id, @Argument ProductInput product) {
        Product updatedProduct = new Product();
        updatedProduct.setName(product.name);
        updatedProduct.setDescription(product.description);
        updatedProduct.setPrice(product.price);
        updatedProduct.setImgUrl(product.imgUrl);

        Set<Category> categoryIdSet = product.categoryIdList().stream()
                .map(categoryId -> Category.builder().id(categoryId).build())
                .collect(Collectors.toSet());
        updatedProduct.getCategories().addAll(categoryIdSet);
        return _productService.updateProduct(id, updatedProduct);
    }

    @MutationMapping
    @Secured("USER")
    public String inactiveByProductId (@Argument Long id) {
        return _productService.inactiveByProductId(id);
    }

    record ProductInput (String name, String description, Double price, String imgUrl, List<Long> categoryIdList) {}
}
