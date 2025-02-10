package com.pancake.convert;

import com.pancake.dto.ProductDto;
import com.pancake.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<Product, ProductDto> {

    @Override
    public ProductDto convert(Product source) {
        ProductDto dto = new ProductDto();
        dto.setId(source.getId());
        dto.setName(source.getName());
        dto.setPrice(source.getPrice());
        dto.setDetails(source.getDetails());
        dto.setIngridients(source.getIngridients());
        return dto;
    }

}
