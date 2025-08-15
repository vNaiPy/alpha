package com.naipy.alpha.modules.product.controller;

import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.model.ProductInput;
import com.naipy.alpha.modules.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService _productService;

    @Autowired
    public ProductController(ProductService productService) {
        this._productService = productService;
    }

    @QueryMapping
    public List<ProductDTO> findAllProducts () {
        return _productService.findAll();
    }

    @QueryMapping
    public ProductDTO findByProductId (@Argument String id) {
        return _productService.findById(id);
    }

    @QueryMapping
    public List<ProductDTO> searchingForWithLngLat (@Argument final String searchingFor, @Argument final Double lng, @Argument final Double lat, @Argument final Double radius) {
        return _productService.searchingForWithLngLat(searchingFor, lng, lat, radius);
    }

    @QueryMapping
    public List<ProductDTO> findAllProductsByOwnerId () {
        return _productService.findAllByOwner();
    }

    @MutationMapping
    @Secured("USER")
    public ProductDTO addProduct (@Argument ProductInput product) {
        return _productService.insert(product);
    }

    @MutationMapping
    @Secured("USER")
    public ProductDTO updateProduct (@Argument ProductDTO productDTO) {
        return _productService.update(productDTO);
    }

    @MutationMapping
    @Secured("USER")
    public String desactivateProductById (@Argument String id) {
        return _productService.deactivate(id);
    }
}
