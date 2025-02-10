package com.pancake.service;

import com.pancake.model.Order;
import com.pancake.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;


    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Order order) {
        orderRepository.delete(order);
        orderRepository.save(order);
    }

    public List<Order> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }
}
