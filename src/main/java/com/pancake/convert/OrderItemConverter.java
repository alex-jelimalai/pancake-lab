package com.pancake.convert;

import com.pancake.dto.OrderItemDto;
import com.pancake.dto.ProductDto;
import com.pancake.model.OrderItem;
import com.pancake.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemConverter implements Converter<OrderItem, OrderItemDto> {


    @Override
    public OrderItemDto convert(OrderItem source) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(source.getId());
        dto.setPrice(source.getPrice());
        dto.setQuantity(source.getQuantity());
        return dto;
    }
}
