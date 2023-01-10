package com.zhuo.springbootmall.service.impl;

import com.zhuo.springbootmall.dao.OrderDao;
import com.zhuo.springbootmall.dao.ProductDao;
import com.zhuo.springbootmall.dao.UserDao;
import com.zhuo.springbootmall.dto.BuyItem;
import com.zhuo.springbootmall.dto.CreateOrderRequest;
import com.zhuo.springbootmall.model.Order;
import com.zhuo.springbootmall.model.OrderItem;
import com.zhuo.springbootmall.model.Product;
import com.zhuo.springbootmall.model.User;
import com.zhuo.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Transactional  // @Transactional 避免數據不一致, 確保 order 跟 order_item 這兩張資料表一定是同時新增,或是同時失敗
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        // 檢查 user 是否存在
        User user = userDao.getUserById(userId);

        if(user == null){
            log.warn("該 userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            // 檢查 product 是否存在, 庫存是否足夠
            if(product == null){
                log.warn("該商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock() < buyItem.getQuantity()){
                log.warn("商品 {} 庫存不足, 無法購買, 剩餘庫存 {}, 欲購買數量 {}",
                        product.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock()- buyItem.getQuantity());

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
