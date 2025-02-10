package com.pancake.convert;

import com.pancake.dto.OrderItemDto;
import com.pancake.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemConverter implements Converter<OrderItem, OrderItemDto> {
    private final ProductConverter productConverter;

    @Override
    public OrderItemDto convert(OrderItem source) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(source.getId());
        dto.setPrice(source.getPrice());
        dto.setQuantity(source.getQuantity());
        dto.setProduct(productConverter.convert(source.getProduct()));
        return dto;
    }
}
