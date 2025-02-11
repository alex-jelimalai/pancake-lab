package com.pancake.convert;

import com.pancake.dto.OrderDto;
import com.pancake.dto.OrderItemDto;
import com.pancake.model.Order;
import com.pancake.model.OrderItem;
import com.pancake.model.OrderStatus;
import com.pancake.model.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderConverterTest {

    @Mock
    private OrderItemConverter orderItemConverter;

    @InjectMocks
    private OrderConverter orderConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void convertOrder() {
        Order order = Order.builder()
                .orderType(OrderType.DISCIPLE)
                .status(OrderStatus.READY_FOR_PROCESSING)
                .building(5)
                .roomNo(102)
                .build();

        OrderItem orderItem = mock(OrderItem.class);
        when(orderItemConverter.convert(orderItem)).thenReturn(new OrderItemDto());
        order.setOrderItems(List.of(orderItem));

        OrderDto orderDto = orderConverter.convert(order);

        assertNotNull(orderDto);
        assertEquals(order.getId(), orderDto.getId());
        assertEquals(order.getBuilding(), orderDto.getBuilding());
        assertEquals(order.getRoomNo(), orderDto.getRoomNo());
        assertEquals(order.getOrderType(), orderDto.getOrderType());
        assertEquals(order.getStatus(), orderDto.getStatus());
        assertEquals(1, orderDto.getItems().size());
    }
}