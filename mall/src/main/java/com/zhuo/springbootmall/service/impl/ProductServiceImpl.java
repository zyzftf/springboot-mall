package com.zhuo.springbootmall.service.impl;

import com.zhuo.springbootmall.constant.ProductCategory;
import com.zhuo.springbootmall.dao.ProductDao;
import com.zhuo.springbootmall.dto.ProductRequest;
import com.zhuo.springbootmall.model.Product;
import com.zhuo.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProducts(ProductCategory category, String search) {
        return productDao.getProducts(category, search);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDao.deleteProduct(productId);
    }
}
