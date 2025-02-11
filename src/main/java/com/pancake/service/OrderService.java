package com.pancake.service;

import com.pancake.dto.OrderDto;
import com.pancake.model.Order;
import com.pancake.model.OrderItem;
import com.pancake.model.OrderStatus;
import com.pancake.repo.OrderRepository;
import com.pancake.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ConversionService conversionService;

    @Transactional
    public void cancelOrder(OrderDto orderDto) {
        deleteOrder(orderDto.getId());
        log.info("Order {} was Canceled and deleted", orderDto.getId());
    }

    @Transactional
    public void deliverOrder(OrderDto orderDto) {
        deleteOrder(orderDto.getId());
        log.info("Order {} was Delivered and deleted", orderDto.getId());
    }

    @Transactional
    public void markReadyToProcess(OrderDto orderDto) {
        orderDto.setStatus(OrderStatus.READY_FOR_PROCESSING);
        saveOrder(orderDto);
        log.info("Order {} was marked ready for into processing", orderDto.getId());
    }

    @Transactional
    public void processOrder(OrderDto orderDto) {
        orderDto.setStatus(OrderStatus.PROCESSING);
        saveOrder(orderDto);
        log.info("Order {} was taken into processing", orderDto.getId());
    }

    @Transactional
    public void completeOrder(OrderDto orderDto) {
        orderDto.setStatus(OrderStatus.COMPLETED);
        saveOrder(orderDto);
        log.info("Order {} was completed", orderDto.getId());
    }

    @Transactional
    public OrderDto saveOrder(OrderDto orderDto) {
        Order order;
        if (orderDto.getId() != null) {
            order = orderRepository.findById(orderDto.getId()).orElseThrow(() -> new IllegalArgumentException("Order not found"));
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
                        .product(productRepository.findById(i.getProduct().getId()).orElseThrow(() -> new IllegalArgumentException("Product not found")))
                        .quantity(i.getQuantity())
                        .build()
        ).collect(toList()));
        return conversionService.convert(orderRepository.save(order), OrderDto.class);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findBy(List<OrderStatus> orderStatuses) {
        return orderRepository.findByStatusIn(orderStatuses).stream().map(order -> conversionService.convert(order, OrderDto.class)).collect(toList());
    }
}
