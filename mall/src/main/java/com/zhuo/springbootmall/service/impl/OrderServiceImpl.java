package com.zhuo.springbootmall.service.impl;

import com.zhuo.springbootmall.dao.OrderDao;
import com.zhuo.springbootmall.dao.ProductDao;
import com.zhuo.springbootmall.dto.BuyItem;
import com.zhuo.springbootmall.dto.CreateOrderRequest;
import com.zhuo.springbootmall.model.Order;
import com.zhuo.springbootmall.model.OrderItem;
import com.zhuo.springbootmall.model.Product;
import com.zhuo.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Transactional  // @Transactional 避免數據不一致, 確保 order 跟 order_item 這兩張資料表一定是同時新增,或是同時失敗
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());


            // 計算總價錢
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            // 轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(amount);
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setProductId(buyItem.getProductId());

            orderItemList.add(orderItem);
        }

        // 創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {

        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsById(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }
}
