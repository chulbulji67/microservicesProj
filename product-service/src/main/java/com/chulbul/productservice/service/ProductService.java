package com.chulbul.productservice.service;

import com.chulbul.productservice.dto.ProductRequest;
import com.chulbul.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();
}
