package com.zhuo.springbootmall.service;

import com.zhuo.springbootmall.dto.CreateOrderRequest;
import com.zhuo.springbootmall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);
}
