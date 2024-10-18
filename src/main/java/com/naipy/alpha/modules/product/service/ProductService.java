package com.naipy.alpha.modules.product.service;

import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.product.repository.ProductRepository;
import com.naipy.alpha.modules.store.repository.StoreRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.exceptions.services.StoreNotRegisteredException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(StoreRepository storeRepository, ProductRepository productRepository) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findAll () {
        return productRepository.findAll().stream().map(ProductDTO::createProductDTO).toList();
    }

    /*public List<ProductDTO> searchingForWithLngLat (String searchingFor, Double lng, Double lat) {
        Double lngMaior = lng + 0.02;
        Double lngMenor = lng - 0.02;
        Double latMaior = lat + 0.02;
        Double latMenor = lat - 0.02;
        return _productRepository.findAllByLngLat(searchingFor.toLowerCase(), lngMaior, lngMenor, latMaior, latMenor).stream().map(ProductDTO::createProductDTO).toList();
    }
*/
    public ProductDTO findById (UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) throw new ResourceNotFoundException("Product not found. Id:" + id);
        return ProductDTO.createProductDTO(productOptional.get());
    }

    public List<ProductDTO> findAllByOwner (UUID id) {
        return productRepository.findAllByOwnerId(id).stream().map(ProductDTO::createProductDTO).toList();
    }

    public ProductDTO insert (Product product, Set<Category> categoryIdSet) {
        if (!storeRepository.existsById(getIdCurrentUser().getId())) throw new StoreNotRegisteredException("Store not found to insert product");
        product.setStatus(ProductStatus.PENDING);
        product.setOwner(getIdCurrentUser());
        Product savedProduct = productRepository.save(product);
        savedProduct.getCategories().addAll(categoryIdSet);
        savedProduct.setStatus(ProductStatus.ACTIVE);
        return ProductDTO.createProductDTO(productRepository.save(savedProduct));
    }

    public ProductDTO updateProduct (UUID id, Product updatedProduct) {
        try {
            Product existentProduct = productRepository.getReferenceById(id);
            updateData(updatedProduct, existentProduct);
            return ProductDTO.createProductDTO(productRepository.save(existentProduct));
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public String inactiveByProductId (UUID id) {
        try {
            Product existentProduct = productRepository.getReferenceById(id);
            existentProduct.setStatus(ProductStatus.INACTIVE);
            productRepository.save(existentProduct);
            return "Product deactivated!";
        }
        catch (EmptyResultDataAccessException e) {
            final String errorMessage = "Product not found by ID: " + id.toString();
            throw new ResourceNotFoundException(errorMessage);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private User getIdCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private void updateData(Product updatedProduct, Product existentProduct) {
        existentProduct.setName(updatedProduct.getName());
        existentProduct.setDescription(updatedProduct.getDescription());
        existentProduct.setPrice(updatedProduct.getPrice());
        existentProduct.setImgUrl(updatedProduct.getImgUrl());
        existentProduct.getCategories().addAll(updatedProduct.getCategories());
    }
}
