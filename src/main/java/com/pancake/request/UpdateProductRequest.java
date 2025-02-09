package com.pancake.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest extends AddProductRequest {
    private Long id;
}
