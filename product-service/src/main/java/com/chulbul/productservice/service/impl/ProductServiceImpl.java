package com.chulbul.productservice.service.impl;

import com.chulbul.productservice.dto.ProductRequest;
import com.chulbul.productservice.dto.ProductResponse;
import com.chulbul.productservice.entity.Product;
import com.chulbul.productservice.repo.ProductRepo;
import com.chulbul.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProductServiceImpl implements ProductService {

    @Autowired
    private  ProductRepo productRepo;

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        Product savedProduct = productRepo.save(product);
        return ProductResponse.builder()
                .id(savedProduct.getId())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .name(savedProduct.getName())
                .build();
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepo.findAll();
        return products.stream().map(this::convertProductToProductResponse).toList();
    }

    private ProductResponse convertProductToProductResponse(Product product) {
        return ProductResponse.builder().id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
