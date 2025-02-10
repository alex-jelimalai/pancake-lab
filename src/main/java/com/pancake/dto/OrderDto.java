package com.pancake.dto;

import com.google.common.collect.ImmutableList;
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

    private OrderStatus status;

    private Integer building;

    private Integer roomNo;

    private final List<OrderItemDto> items = new ArrayList<>();

    public void addItem(OrderItemDto item) {
        items.add(item);
    }

}
