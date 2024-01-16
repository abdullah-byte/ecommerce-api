package com.happy.ecommerce.dto.order;

import lombok.Data;

import java.util.List;
@Data
public class OrderRequest {
    private int userId;
    private List<ProductItem> productList;

    @Data
    public static class ProductItem {
        private int productId;
        private int quantity;
    }
}
