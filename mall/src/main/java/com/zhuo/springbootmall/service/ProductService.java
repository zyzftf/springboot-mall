package com.zhuo.springbootmall.service;

import com.zhuo.springbootmall.dto.ProductQueryParams;
import com.zhuo.springbootmall.dto.ProductRequest;
import com.zhuo.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    Integer countProduct(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProduct(Integer productId);
}
