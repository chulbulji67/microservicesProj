package com.chulbul.orderservice.service;

import com.chulbul.orderservice.dto.OrderRequest;

public interface OrderService {

    public void placeOrder(OrderRequest orderRequest);
}
