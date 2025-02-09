package com.pancake.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductRequest {
    private String name;
    private String ingridients;
    private String recipe;
    private double price;

}
