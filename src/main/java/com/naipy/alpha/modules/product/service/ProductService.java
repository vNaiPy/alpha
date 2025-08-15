package com.naipy.alpha.modules.product.service;

import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.product.model.ProductInput;
import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.product.repository.ProductRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.utils.ServiceUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService extends ServiceUtils {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findAll () {
        return productRepository.findAll().stream().map(ProductDTO::createProductDTO).toList();
    }

    public List<ProductDTO> searchingForWithLngLat (String searchingFor, Double lng, Double lat, Double radius) {
        Double lngMaior = lng + radius;
        Double lngMenor = lng - radius;
        Double latMaior = lat + radius;
        Double latMenor = lat - radius;
        return productRepository.findAllByLngLat(searchingFor.toLowerCase(), lngMaior, lngMenor, latMaior, latMenor)
                .stream().map(ProductDTO::createProductDTO)
                .toList();
    }

    public ProductDTO findById (String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) throw new ResourceNotFoundException("Product not found. Id:" + id);
        return ProductDTO.createProductDTO(productOptional.get());
    }

    public List<ProductDTO> findAllByOwner () {
        return productRepository.findAllByOwnerId(getIdCurrentUser().getId()).stream().map(ProductDTO::createProductDTO).toList();
    }

    public List<ProductDTO> findAllByNameContainingIgnoreCase (String name) {
        return productRepository.findAllByNameContainingIgnoreCase(name).stream().map(ProductDTO::createProductDTO).toList();
    }

    @Transactional
    public ProductDTO insert (final ProductInput productInput) {
        Product product = Product.builder()
                .id(generateUUID())
                .name(productInput.name())
                .description(productInput.description())
                .price(productInput.price())
                .imgUrl(productInput.imgUrl())
                .status(ProductStatus.ACTIVE)
                .owner(getIdCurrentUser())
                .categories(productInput.categories())
                .build();
        return ProductDTO.createProductDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO update (ProductDTO updatedProduct) {
        try {
            Product existentProduct = productRepository.getReferenceById(getIdCurrentUser().getId());
            updateData(updatedProduct, existentProduct);
            return ProductDTO.createProductDTO(productRepository.save(existentProduct));
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public String deactivate (String id) {
        try {
            Product existentProduct = productRepository.getReferenceById(id);
            existentProduct.setStatus(ProductStatus.DESACTIVATED);
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

    private void updateData(ProductDTO updatedProduct, Product existentProduct) {
        existentProduct.setName(updatedProduct.name());
        existentProduct.setDescription(updatedProduct.description());
        existentProduct.setPrice(updatedProduct.price());
        existentProduct.setImgUrl(updatedProduct.imgUrl());
        existentProduct.setCategories(updatedProduct.categories());
    }
}
