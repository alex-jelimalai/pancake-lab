package com.pancake.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemDto {

    private Long id;

    private ProductDto product;

    private int quantity;
    private double price;


}
