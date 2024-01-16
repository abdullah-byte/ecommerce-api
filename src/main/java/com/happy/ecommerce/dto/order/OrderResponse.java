package com.happy.ecommerce.dto.order;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private int id;
    private int userId;
    private String orderDate;
    private double totalAmount;
    private List<OrderItem> orderItemList;
    @Data
    @Builder
    public static class OrderItem {
        private int productId;
        private int quantity;
        private double subtotal;
    }
}
