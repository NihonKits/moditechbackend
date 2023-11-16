package com.moditech.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class TopSoldProductDto {
    private String id;
    private String barcode;
    private String productName;
    private String productImage;
    private String description;
    private String isAd;
    private List<ProductVariationsDto> productVariationsList;
    private int totalSold;
}