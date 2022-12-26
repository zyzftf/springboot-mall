package com.zhuo.springbootmall.service.impl;

import com.zhuo.springbootmall.dao.ProductDao;
import com.zhuo.springbootmall.model.Product;
import com.zhuo.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }
}
