package com.moditech.ecommerce.dto;

import lombok.Data;

@Data
public class OrderDetailsDto {
    private ProductDto product;
    private int variationIndex;
    private int quantity;
}
