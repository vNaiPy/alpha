package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Product;
import com.naipy.alpha.entities.User;
import com.naipy.alpha.entities.dto.ProductDTO;
import com.naipy.alpha.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService _productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll () {
        List<ProductDTO> productList = _productService.findAll();
        return ResponseEntity.ok().body(productList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findById (@PathVariable Long id) {
        Product product = _productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value = "/store/{id}")
    public ResponseEntity<List<ProductDTO>> findAllByOwnerId (@PathVariable Long id) {
        List<ProductDTO> productDTOList = _productService.findAllByOwner(id);
        return ResponseEntity.ok().body(productDTOList);
    }

    @PostMapping
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
    }
}
