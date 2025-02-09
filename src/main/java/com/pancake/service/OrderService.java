package com.pancake.service;

import com.pancake.model.Order;
import com.pancake.repo.OrderRepository;
import com.pancake.request.CreateOrderRequest;
import com.pancake.request.GetOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Order> getOrders(GetOrderRequest getOrderRequest) {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        throw new RuntimeException(" not implemented yet");
    }


    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("order not found"));
        order.cancel();
        orderRepository.save(order);
    }

}
