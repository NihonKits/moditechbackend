package com.moditech.ecommerce.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductVariations {

    private String variationName;

    private Double price;

    private String imgUrl;

    private Integer quantity;

    private String description;

    private Integer sold = 0;

}
