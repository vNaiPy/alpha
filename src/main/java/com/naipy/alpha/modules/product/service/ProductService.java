package com.naipy.alpha.modules.product.service;

import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.product.model.ProductInput;
import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.product.model.SearchingProductInput;
import com.naipy.alpha.modules.product.repository.ProductRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.utils.GeoUtils;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.utils.models.ItemsPaginatorResponse;
import com.naipy.alpha.modules.utils.models.PaginatorInput;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
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

    public ItemsPaginatorResponse<ProductDTO> searchingForWithLngLat (SearchingProductInput searchingProductInput) {
        GeoUtils.BoundingBox box = GeoUtils.calculateBoundingBox(searchingProductInput.lat(), searchingProductInput.lng(), searchingProductInput.radius());
        Page<Product> productPage =  productRepository.findAllWithinRadiusByLngLat(searchingProductInput.searchingFor().toLowerCase(),
                searchingProductInput.lng(),
                searchingProductInput.lat(),
                searchingProductInput.radius(),
                box.lngMax(),
                box.lngMin(),
                box.latMax(),
                box.latMin(),
                createPageable(searchingProductInput.paginatorInput()));
        List<ProductDTO> productDTOList = productPage.stream().map(ProductDTO::createProductDTO).toList();
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), searchingProductInput.paginatorInput().page());
    }

    public ProductDTO findById (String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) throw new ResourceNotFoundException("Product not found. Id:" + id);
        return ProductDTO.createProductDTO(productOptional.get());
    }

    public List<ProductDTO> findAllByOwner (PaginatorInput paginatorInput) {
        return productRepository.findAllByOwnerId(getIdCurrentUser().getId(), createPageable(paginatorInput)).stream().map(ProductDTO::createProductDTO).toList();
    }

    public List<ProductDTO> findAllByNameContainingIgnoreCase (String name, PaginatorInput paginatorInput) {
        return productRepository.findAllByNameContainingIgnoreCase(name, createPageable(paginatorInput)).stream().map(ProductDTO::createProductDTO).toList();
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
            final String errorMessage = "Product not found by ID: " + id;
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
