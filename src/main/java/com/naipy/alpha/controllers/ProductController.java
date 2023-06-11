package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Category;
import com.naipy.alpha.entities.Product;
import com.naipy.alpha.entities.dto.ProductDTO;
import com.naipy.alpha.services.ProductService;
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
    @Secured("USER")
    public List<ProductDTO> findAllProducts () {
        return _productService.findAll();
    }

    @QueryMapping
    public ProductDTO findByProductId (@Argument Long id) {
        return _productService.findById(id);
    }

    @QueryMapping
    public List<ProductDTO> findAllProductsByOwnerId (@Argument Long id) {
        return _productService.findAllByOwner(id);
    }

    @MutationMapping
    public ProductDTO addProducts (@Argument ProductInput productList) {
        Product newProduct = new Product();
        newProduct.setName(productList.title);
        newProduct.setDescription(productList.description);
        newProduct.setPrice(productList.price);
        newProduct.setImgUrl(productList.imgUrl);

        Set<Category> categoryIdSet = productList.categoryIdList().stream()
                .map(categoryId -> Category.builder().id(categoryId).build())
                .collect(Collectors.toSet());
        return _productService.insert(newProduct, categoryIdSet);
    }

    record ProductInput (String title, String description, Double price, String imgUrl, List<Long> categoryIdList) {}

    /*@PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update (@PathVariable Long id, @RequestBody Product product) {
        ProductDTO productDTO = _productService.update(id, product);
        return ResponseEntity.ok().body(productDTO);
    }*/

    /*@DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        _productService.delete(id);
        return ResponseEntity.noContent().build();
    }*/
}
