package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Product;
import com.naipy.alpha.entities.User;
import com.naipy.alpha.entities.dto.ProductDTO;
import com.naipy.alpha.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public List<ProductDTO> findAllProductsByOwnerId (@Argument Long id) {
        return _productService.findAllByOwner(id);
    }

    /*@PostMapping
    public ResponseEntity<ProductDTO> insert (@RequestBody Product product) {
        ProductDTO productDTO = _productService.insert(product);
        URI location = UtilsForController.getURI(productDTO.getId());
        return ResponseEntity.created(location).body(productDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update (@PathVariable Long id, @RequestBody Product product) {
        ProductDTO productDTO = _productService.update(id, product);
        return ResponseEntity.ok().body(productDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        _productService.delete(id);
        return ResponseEntity.noContent().build();
    }*/
}
