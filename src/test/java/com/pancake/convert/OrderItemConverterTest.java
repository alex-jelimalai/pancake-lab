package com.pancake.convert;

import com.pancake.dto.OrderItemDto;
import com.pancake.dto.ProductDto;
import com.pancake.model.OrderItem;
import com.pancake.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderItemConverterTest {

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private OrderItemConverter orderItemConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void convertConvertOrderItemToOrderItemDto() {
        Product product = mock(Product.class);
        ProductDto productDto = new ProductDto();
        when(productConverter.convert(product)).thenReturn(productDto);

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(3)
                .price(12.99)
                .build();

        OrderItemDto orderItemDto = orderItemConverter.convert(orderItem);

        assertNotNull(orderItemDto);
        assertEquals(orderItem.getId(), orderItemDto.getId());
        assertEquals(orderItem.getQuantity(), orderItemDto.getQuantity());
        assertEquals(orderItem.getPrice(), orderItemDto.getPrice());
        assertEquals(productDto, orderItemDto.getProduct());
    }

}