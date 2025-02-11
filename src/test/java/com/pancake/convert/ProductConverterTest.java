package com.pancake.convert;

import com.pancake.dto.ProductDto;
import com.pancake.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductConverterTest {

    private final ProductConverter productConverter = new ProductConverter();

    @Test
    void convertProduct() {
        Product product = Product.builder()
                .name("Chocolate Pancake")
                .price(5.99)
                .details("Delicious chocolate pancake")
                .ingridients("Flour, Milk, Cocoa, Sugar")
                .build();

        ProductDto productDto = productConverter.convert(product);

        assertNotNull(productDto);
        assertEquals(product.getId(), productDto.getId());
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getPrice(), productDto.getPrice());
        assertEquals(product.getDetails(), productDto.getDetails());
        assertEquals(product.getIngridients(), productDto.getIngridients());
    }
}












