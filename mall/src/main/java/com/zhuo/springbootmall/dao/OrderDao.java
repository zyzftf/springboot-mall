package com.zhuo.springbootmall.dao;

import com.zhuo.springbootmall.dto.CreateOrderRequest;
import com.zhuo.springbootmall.dto.OrderQueryParams;
import com.zhuo.springbootmall.model.Order;
import com.zhuo.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrder(OrderQueryParams orderQueryParams);
}
