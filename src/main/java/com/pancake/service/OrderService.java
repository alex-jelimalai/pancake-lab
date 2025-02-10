package com.pancake.service;

import com.pancake.dto.OrderDto;
import com.pancake.model.Order;
import com.pancake.model.OrderItem;
import com.pancake.repo.OrderRepository;
import com.pancake.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ConversionService conversionService;

    @Transactional
    public OrderDto saveOrder(OrderDto orderDto) {
        Order order;
        if (orderDto.getId() != null) {
            order = orderRepository.findById(orderDto.getId()).orElseThrow();
        } else {
            order = new Order();
        }
        order.setBuilding(orderDto.getBuilding());
        order.setOrderType(orderDto.getOrderType());
        order.setRoomNo(orderDto.getRoomNo());
        order.setStatus(orderDto.getStatus());
        order.setOrderItems(orderDto.getItems().stream().map(i ->
                OrderItem.builder()
                        .order(order)
                        .price(i.getPrice())
                        .product(productRepository.findById(i.getProduct().getId()).orElseThrow())
                        .quantity(i.getQuantity())
                        .build()
        ).collect(Collectors.toList()));
        return conversionService.convert(orderRepository.save(order), OrderDto.class);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream().map(order -> conversionService.convert(order, OrderDto.class)).collect(Collectors.toList());
    }
}
