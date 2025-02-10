package com.pancake.convert;

import com.pancake.dto.OrderDto;
import com.pancake.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConverter implements Converter<Order, OrderDto> {
    private final OrderItemConverter orderItemConverter;


    @Override
    public OrderDto convert(Order source) {
        OrderDto dto = new OrderDto();
        dto.setId(source.getId());
        dto.setBuilding(source.getBuilding());
        dto.setRoomNo(source.getRoomNo());
        dto.setOrderType(source.getOrderType());
        dto.setStatus(source.getStatus());
        source.getItems().stream().map(orderItemConverter::convert).forEach(dto::addItem);
        return dto;
    }
}
