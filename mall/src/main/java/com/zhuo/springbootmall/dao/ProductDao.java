package com.zhuo.springbootmall.dao;

import com.zhuo.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

}
