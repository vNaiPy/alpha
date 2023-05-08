package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Product;
import com.naipy.alpha.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService _productService;

    @GetMapping
    public ResponseEntity<List<Product>> findAll () {
        List<Product> productList = _productService.findAll();
        return ResponseEntity.ok().body(productList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findById (@PathVariable Long id) {
        Product product = _productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    public ResponseEntity insert (@RequestBody Product product) {
        return null;
    }

}
