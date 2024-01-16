package com.happy.ecommerce.dto.product;


import lombok.Data;

@Data
public class ProductResponse {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
}
