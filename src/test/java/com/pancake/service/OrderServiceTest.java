package com.pancake.service;

import com.pancake.convert.ProductConverter;
import com.pancake.dto.OrderDto;
import com.pancake.dto.OrderItemDto;
import com.pancake.model.Order;
import com.pancake.model.OrderStatus;
import com.pancake.model.OrderType;
import com.pancake.model.Product;
import com.pancake.repo.OrderRepository;
import com.pancake.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ConversionService conversionService;
    private final ProductConverter productConverter = new ProductConverter();

    @InjectMocks
    private OrderService orderService;

    private OrderDto orderDto;
    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 

        product = new Product("Product1", "Flour, Sugar", "Product details", 10.0);
        product.setId(1L);

        order = new Order(OrderType.DELIVERY, OrderStatus.PENDING, 1, 101);
        order.setId(1L);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setOrderType(OrderType.DELIVERY);
        orderDto.setStatus(OrderStatus.PENDING);
        orderDto.setBuilding(1);
        orderDto.setRoomNo(101);

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProduct(productConverter.convert(product));
        itemDto.setQuantity(2);
        itemDto.setPrice(20.0);
        orderDto.setItems(List.of(itemDto));
    }

    @Test
    void saveOrder_ShouldSaveNewOrder_WhenOrderDtoIsNew() {

        orderDto.setId(null);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(conversionService.convert(any(Order.class), eq(OrderDto.class))).thenReturn(orderDto);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));


        OrderDto result = orderService.saveOrder(orderDto);


        assertNotNull(result);
        assertEquals(orderDto.getOrderType(), result.getOrderType());
        assertEquals(orderDto.getStatus(), result.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void saveOrder_ShouldUpdateExistingOrder_WhenOrderDtoExists() {

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(conversionService.convert(any(Order.class), eq(OrderDto.class))).thenReturn(orderDto);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));


        OrderDto result = orderService.saveOrder(orderDto);


        assertNotNull(result);
        assertEquals(orderDto.getOrderType(), result.getOrderType());
        assertEquals(orderDto.getStatus(), result.getStatus());
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository).findById(1L);
    }

    @Test
    void saveOrder_ShouldThrowException_WhenProductNotFound() {

        orderDto.getItems().get(0).setProduct(null);

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.saveOrder(orderDto);
        });
    }

    @Test
    void deleteOrder_ShouldCallDeleteById_WhenOrderExists() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        orderService.deleteOrder(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void deleteOrder_ShouldNotCallDelete_WhenOrderDoesNotExist() {

        when(orderRepository.existsById(1L)).thenReturn(false);


        orderService.deleteOrder(1L);


        verify(orderRepository, never()).deleteById(1L);
    }

    @Test
    void findAll_ShouldReturnOrderDtos() {

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(conversionService.convert(any(Order.class), eq(OrderDto.class))).thenReturn(orderDto);


        List<OrderDto> result = orderService.findAll();


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDto.getOrderType(), result.get(0).getOrderType());
        verify(orderRepository).findAll();
    }
}
