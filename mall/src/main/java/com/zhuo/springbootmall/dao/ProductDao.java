package com.zhuo.springbootmall.dao;

import com.zhuo.springbootmall.dto.ProductRequest;
import com.zhuo.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);
}
