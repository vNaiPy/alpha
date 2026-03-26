package com.naipy.alpha.modules.product.service.impl;

import com.naipy.alpha.modules.exceptions.services.ProductNotFoundException;
import com.naipy.alpha.modules.exceptions.services.StoreNotRegisteredException;
import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.product.model.ProductInput;
import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.product.model.SearchingProductInput;
import com.naipy.alpha.modules.product.repository.ProductRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.product.service.ProductService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository _productRepository;

    private final StoreRepository _storeRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, StoreRepository storeRepository) {
        this._productRepository = productRepository;
        this._storeRepository = storeRepository;
    }

    public ItemsPaginatorResponse<ProductDTO> getAllProducts (PaginatorInput paginatorInput) {
        Page<Product> productPage = _productRepository.findAll(ServiceUtils.createPageable(paginatorInput));
        List<ProductDTO> productDTOList =  ProductDTO.productPageToProductDTOList(productPage);
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), paginatorInput.page());
    }

    public ItemsPaginatorResponse<ProductDTO> getProductsByContainingNameAndLngLat (SearchingProductInput searchingProductInput) {
        GeoUtils.BoundingBox box = GeoUtils.calculateBoundingBox(searchingProductInput.lat(), searchingProductInput.lng(), searchingProductInput.radius());
        Page<Product> productPage =  _productRepository.findAllWithinRadiusByLngLat(searchingProductInput.searchingFor().toLowerCase(),
                searchingProductInput.lng(),
                searchingProductInput.lat(),
                searchingProductInput.radius(),
                box.lngMax(),
                box.lngMin(),
                box.latMax(),
                box.latMin(),
                ServiceUtils.createPageable(searchingProductInput.paginatorInput()));
        List<ProductDTO> productDTOList = ProductDTO.productPageToProductDTOList(productPage);
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), searchingProductInput.paginatorInput().page());
    }

    public ProductDTO getProductById (String id) {
        Optional<Product> productOptional = _productRepository.findById(id);
        if (productOptional.isEmpty()) throw new ResourceNotFoundException("Product not found. Id:" + id);
        return ProductDTO.createProductDTO(productOptional.get());
    }

    public ItemsPaginatorResponse<ProductDTO> getProductsByOwner (PaginatorInput paginatorInput) {
        validateStoreExistence(ServiceUtils.getCurrentUser().getId());
        Page<Product> productPage = _productRepository.findAllByOwnerId(ServiceUtils.getCurrentUser().getId(), ServiceUtils.createPageable(paginatorInput));
        List<ProductDTO> productDTOList = ProductDTO.productPageToProductDTOList(productPage);
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), paginatorInput.page());
    }

    public ItemsPaginatorResponse<ProductDTO> getProductsByOwner (String ownerId, PaginatorInput paginatorInput) {
        Page<Product> productPage = _productRepository.findAllByOwnerId(ownerId, ServiceUtils.createPageable(paginatorInput));
        List<ProductDTO> productDTOList = ProductDTO.productPageToProductDTOList(productPage);
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), paginatorInput.page());
    }

    public ItemsPaginatorResponse<ProductDTO> findAllByNameContainingIgnoreCase (String name, PaginatorInput paginatorInput) {
        Page<Product> productPage = _productRepository.findAllByNameContainingIgnoreCase(name, ServiceUtils.createPageable(paginatorInput));
        List<ProductDTO> productDTOList = ProductDTO.productPageToProductDTOList(productPage);
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(), paginatorInput.page());
    }

    public ItemsPaginatorResponse<ProductDTO> getProductsByCategory (SearchingProductInput searchingProductInput) {
        Page<Product> productPage = _productRepository.findAllByCategory(searchingProductInput.categoryIds(), ServiceUtils.createPageable(searchingProductInput.paginatorInput()));
        List<ProductDTO> productDTOList = ProductDTO.productPageToProductDTOList(productPage);
        return new ItemsPaginatorResponse<>(productDTOList, productPage.getTotalElements(), productPage.getTotalPages(),searchingProductInput.paginatorInput().page());
    }

    @Transactional
    public ProductDTO insertProduct (ProductInput productInput) {
        validateStoreExistence(ServiceUtils.getCurrentUser().getId());
        Product product = Product.builder()
                .id(ServiceUtils.generateUUID())
                .name(productInput.name())
                .description(productInput.description())
                .price(productInput.price())
                .imgUrl(productInput.imgUrl())
                .status(ProductStatus.ACTIVE)
                .owner(ServiceUtils.getCurrentUser())
                .categories(productInput.categories())
                .build();
        return ProductDTO.createProductDTO(_productRepository.save(product));
    }

    @Transactional
    public ProductDTO updateProduct (ProductInput product) {
        try {
            Product existentProduct = _productRepository.getReferenceById(product.id());
            updateData(product, existentProduct);
            return ProductDTO.createProductDTO(_productRepository.save(existentProduct));
        }
        catch (EntityNotFoundException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public String deactivateProduct (String id) {
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

    @Transactional
    public String deactivateAllProducts (List<String> ids) {
        List<Product> existentProductList = new ArrayList<>();
        for (String id : ids) {
            try {
                Product existentProduct = _productRepository.getReferenceById(id);
                existentProduct.setStatus(ProductStatus.DESACTIVATED);
                existentProductList.add(existentProduct);
            }
            catch (EmptyResultDataAccessException e) {
                final String errorMessage = "Product not found by ID: " + id;
                throw new ResourceNotFoundException(errorMessage);
            }
        }
        try {
            _productRepository.saveAll(existentProductList);
            return "Products deactivated!";
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
            throw new StoreNotRegisteredException("You need to create a store!");
    }
}
