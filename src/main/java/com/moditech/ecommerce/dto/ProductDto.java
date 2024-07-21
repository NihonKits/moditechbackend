package com.moditech.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moditech.ecommerce.model.ProductVariations;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {

    private String id;

    private String barcode;

    private String productName;

    private String productImage;

    private String description;

    private String isAd = "false";

    private List<ProductVariations> productVariationsList;
}
