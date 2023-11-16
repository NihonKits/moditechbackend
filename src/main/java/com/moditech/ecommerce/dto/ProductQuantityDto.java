package com.moditech.ecommerce.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductQuantityDto {

    private String productId;
    private List<Integer> variationIndexes = new ArrayList<>(); // Assuming you want to track the variation by name
    private int quantity;
}