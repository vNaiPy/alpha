package com.naipy.alpha.services;

import com.naipy.alpha.entities.Product;
import com.naipy.alpha.entities.User;
import com.naipy.alpha.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository _productRepository;

    public List<Product> findAll () {
        return _productRepository.findAll();
    }

    public Product findById (Long id) {
        Optional<Product> productOptional = _productRepository.findById(id);
        return productOptional.get();
    }

    private User getIdCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
