package com.pancake.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String ingridients;
    private String recipe;
    private double price;


}
