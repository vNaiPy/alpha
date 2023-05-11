package com.naipy.alpha.services;

import com.naipy.alpha.entities.Product;
import com.naipy.alpha.entities.User;
import com.naipy.alpha.entities.dto.ProductDTO;
import com.naipy.alpha.repositories.ProductRepository;
import com.naipy.alpha.services.exceptions.DatabaseException;
import com.naipy.alpha.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository _productRepository;

    public List<ProductDTO> findAll () {
        return _productRepository.findAll().stream().map(ProductDTO::createProductDTO).collect(Collectors.toList());
    }

    public Product findById (Long id) {
        Optional<Product> productOptional = _productRepository.findById(id);
        return productOptional.get();
    }

    public ProductDTO insert (Product product) {
        product.setOwner(getIdCurrentUser());
        return ProductDTO.createProductDTO(_productRepository.save(product));
    }

    public ProductDTO update (Long id, Product product) {
        try {
            Product entity = _productRepository.getReferenceById(id);
            updateData(product, entity);
            return ProductDTO.createProductDTO(_productRepository.save(entity));
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public void delete (Long id) {
        try {
            _productRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private User getIdCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private void updateData(Product product, Product entity) {
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setImgUrl(product.getImgUrl());
    }
}
