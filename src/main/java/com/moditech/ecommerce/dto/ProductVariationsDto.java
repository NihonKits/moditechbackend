package com.moditech.ecommerce.dto;

import lombok.Data;

@Data
public class ProductVariationsDto {

    private String variationName;

    private Double price;

    private String imgUrl;

    private Integer quantity;

    private String description;

    private Integer sold = 0;
}