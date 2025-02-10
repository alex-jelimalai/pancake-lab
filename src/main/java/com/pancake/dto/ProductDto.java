package com.pancake.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDto {

    private Long id;

    private String name;

    private String ingridients;

    private String details;

    private double price;

}
