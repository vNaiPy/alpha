package com.naipy.alpha.modules.product.service;

import com.naipy.alpha.modules.product.model.ProductDTO;
import com.naipy.alpha.modules.product.model.ProductInput;
import com.naipy.alpha.modules.product.model.SearchingProductInput;
import com.naipy.alpha.modules.utils.models.ItemsPaginatorResponse;
import com.naipy.alpha.modules.utils.models.PaginatorInput;

import java.util.List;

public interface ProductService {

    ItemsPaginatorResponse<ProductDTO> getAllProducts (PaginatorInput paginatorInput);
    ItemsPaginatorResponse<ProductDTO> getProductsByContainingNameAndLngLat (SearchingProductInput searchingProductInput);
    ProductDTO getProductById (String id);
    ItemsPaginatorResponse<ProductDTO> getProductsByOwner (PaginatorInput paginatorInput);
    ItemsPaginatorResponse<ProductDTO> getProductsByOwner (String ownerId, PaginatorInput paginatorInput);
    ItemsPaginatorResponse<ProductDTO> getProductsByCategory (SearchingProductInput searchingProductInput);
    ProductDTO insertProduct (ProductInput productInput);
    ProductDTO updateProduct (ProductInput product);
    String deactivateProduct (String id);
    String deactivateAllProducts (List<String> ids);
}
