package com.naipy.alpha.modules.product.controller;

import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.model.ProductInput;
import com.naipy.alpha.modules.product.model.SearchingProductInput;
import com.naipy.alpha.modules.product.service.ProductService;
import com.naipy.alpha.modules.utils.models.ItemsPaginatorResponse;
import com.naipy.alpha.modules.utils.models.PaginatorInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public ItemsPaginatorResponse<ProductDTO> findAllProducts (@Argument PaginatorInput paginatorInput) {
        return productService.getAllProducts(paginatorInput);
    }

    @QueryMapping
    public ProductDTO findByProductId (@Argument String id) {
        return productService.getProductById(id);
    }

    @QueryMapping
    public ItemsPaginatorResponse<ProductDTO> findAllWithinRadiusByLngLat (@Argument SearchingProductInput searchingProductInput) {
        return productService.getProductsByContainingNameAndLngLat(searchingProductInput);
    }

    @QueryMapping
    public ItemsPaginatorResponse<ProductDTO> findAllMyProducts (@Argument PaginatorInput paginatorInput) {
        return productService.getProductsByOwner(paginatorInput);
    }

    @QueryMapping
    public ItemsPaginatorResponse<ProductDTO> findAllMyProductsByOwnerId (@Argument final String ownerId, @Argument PaginatorInput paginatorInput) {
        return productService.getProductsByOwner(ownerId, paginatorInput);
    }

    @QueryMapping
    public ItemsPaginatorResponse<ProductDTO> findAllByCategory (@Argument SearchingProductInput searchingProductInput) {
        return productService.getProductsByCategory(searchingProductInput);
    }

    @MutationMapping
    @Secured("USER")
    public ProductDTO addProduct (@Argument ProductInput product) {
        return productService.insertProduct(product);
    }

    @MutationMapping
    @Secured("USER")
    public ProductDTO updateProduct (@Argument ProductInput product) {
        return productService.updateProduct(product);
    }

    @MutationMapping
    @Secured("USER")
    public String desactivateProductById (@Argument final String id) {
        return productService.deactivateProduct(id);
    }

    @MutationMapping
    @Secured("USER")
    public String desactivateAllProductsByIds (@Argument final List<String> ids) {
        return productService.deactivateAllProducts(ids);
    }
}
