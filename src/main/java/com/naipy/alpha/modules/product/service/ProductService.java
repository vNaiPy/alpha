package com.naipy.alpha.modules.product.service;

import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.product.model.ProductInput;
import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.product.model.SearchingProductInput;
import com.naipy.alpha.modules.product.repository.ProductRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.store.repository.StoreRepository;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService extends ServiceUtils {

    private final ProductRepository _productRepository;

    private final StoreRepository _storeRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, StoreRepository storeRepository) {
        this._productRepository = productRepository;
        this._storeRepository = storeRepository;
    }

    public ItemsPaginatorResponse<ProductDTO> findAll (PaginatorInput paginatorInput) {
        Page<Product> productPage = _productRepository.findAll(createPageable(paginatorInput));
        List<ProductDTO> productDTOList =  productPage.stream().map(ProductDTO::createProductDTO).toList();
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), paginatorInput.page());
    }

    public ItemsPaginatorResponse<ProductDTO> searchingForWithLngLat (SearchingProductInput searchingProductInput) {
        GeoUtils.BoundingBox box = GeoUtils.calculateBoundingBox(searchingProductInput.lat(), searchingProductInput.lng(), searchingProductInput.radius());
        Page<Product> productPage =  _productRepository.findAllWithinRadiusByLngLat(searchingProductInput.searchingFor().toLowerCase(),
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
        Optional<Product> productOptional = _productRepository.findById(id);
        if (productOptional.isEmpty()) throw new ResourceNotFoundException("Product not found. Id:" + id);
        return ProductDTO.createProductDTO(productOptional.get());
    }

    public ItemsPaginatorResponse<ProductDTO> findAllByOwner (PaginatorInput paginatorInput) {
        validateStoreExistence(getCurrentUser().getId());
        Page<Product> productPage = _productRepository.findAllByOwnerId(getCurrentUser().getId(), createPageable(paginatorInput));
        List<ProductDTO> productDTOList =  productPage.stream().map(ProductDTO::createProductDTO).toList();
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), paginatorInput.page());
    }

    public ItemsPaginatorResponse<ProductDTO> findAllByOwner (String ownerId, PaginatorInput paginatorInput) {
        validateStoreExistence(ownerId);
        Page<Product> productPage = _productRepository.findAllByOwnerId(ownerId, createPageable(paginatorInput));
        List<ProductDTO> productDTOList =  productPage.stream().map(ProductDTO::createProductDTO).toList();
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), paginatorInput.page());
    }

    public ItemsPaginatorResponse<ProductDTO> findAllByNameContainingIgnoreCase (String name, PaginatorInput paginatorInput) {
        Page<Product> productPage = _productRepository.findAllByNameContainingIgnoreCase(name, createPageable(paginatorInput));
        List<ProductDTO> productDTOList =  productPage.stream().map(ProductDTO::createProductDTO).toList();
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), paginatorInput.page());
    }

    public ItemsPaginatorResponse<ProductDTO> findAllByCategory (SearchingProductInput searchingProductInput) {
        Page<Product> productPage = _productRepository.findAllByCategory(searchingProductInput.categoryIds(), createPageable(searchingProductInput.paginatorInput()));
        List<ProductDTO> productDTOList =  productPage.stream().map(ProductDTO::createProductDTO).toList();
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(),searchingProductInput.paginatorInput().page());
    }

    @Transactional
    public ProductDTO insert (ProductInput productInput) {
        validateStoreExistence(getCurrentUser().getId());
        Product product = Product.builder()
                .id(generateUUID())
                .name(productInput.name())
                .description(productInput.description())
                .price(productInput.price())
                .imgUrl(productInput.imgUrl())
                .status(ProductStatus.ACTIVE)
                .owner(getCurrentUser())
                .categories(productInput.categories())
                .build();
        return ProductDTO.createProductDTO(_productRepository.save(product));
    }

    @Transactional
    public ProductDTO update (ProductInput product) {
        try {
            Product existentProduct = _productRepository.getReferenceById(getCurrentUser().getId());
            updateData(product, existentProduct);
            return ProductDTO.createProductDTO(_productRepository.save(existentProduct));
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public String deactivate (String id) {
        try {
            Product existentProduct = _productRepository.getReferenceById(id);
            existentProduct.setStatus(ProductStatus.DESACTIVATED);
            _productRepository.save(existentProduct);
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

    private void updateData(ProductInput updatedProduct, Product existentProduct) {
        existentProduct.setName(updatedProduct.name());
        existentProduct.setDescription(updatedProduct.description());
        existentProduct.setPrice(updatedProduct.price());
        existentProduct.setImgUrl(updatedProduct.imgUrl());
        existentProduct.setCategories(updatedProduct.categories());
    }

    private void validateStoreExistence(String ownerId) {
        if (Boolean.FALSE.equals(_storeRepository.existsByOwnerId(ownerId)))
            throw new EntityNotFoundException("You need to create a store!");
    }
}
