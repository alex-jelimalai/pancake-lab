package com.pancake.dto;

import com.pancake.model.OrderStatus;
import com.pancake.model.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class OrderDto {

    private Long id;

    private OrderType orderType;

    private OrderStatus status = OrderStatus.NEW;

    private Integer building;

    private Integer roomNo;


    public Double getTotal$() {
        return getItems().stream().mapToDouble(s -> s.getQuantity() * s.getPrice()).sum();
    }

    private List<OrderItemDto> items = new ArrayList<>();

    public void addItem(OrderItemDto item) {
        items.add(item);
    }

}
