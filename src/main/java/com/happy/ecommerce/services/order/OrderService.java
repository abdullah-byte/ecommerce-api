package com.happy.ecommerce.services.order;

import com.happy.ecommerce.dto.order.OrderRequest;
import com.happy.ecommerce.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {
   OrderResponse createOrder(OrderRequest orderRequest);
   List<OrderResponse> getOrders();
   OrderResponse getOrder(int orderId);
}
