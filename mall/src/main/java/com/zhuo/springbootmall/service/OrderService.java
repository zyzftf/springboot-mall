package com.zhuo.springbootmall.service;

import com.zhuo.springbootmall.dto.CreateOrderRequest;
import com.zhuo.springbootmall.dto.OrderQueryParams;
import com.zhuo.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrder(OrderQueryParams orderQueryParams);
}
