package com.happy.ecommerce.services.order;

import com.happy.ecommerce.dto.order.OrderRequest;
import com.happy.ecommerce.dto.order.OrderResponse;
import com.happy.ecommerce.entities.Order;
import com.happy.ecommerce.entities.OrderItem;
import com.happy.ecommerce.entities.Product;
import com.happy.ecommerce.entities.User;
import com.happy.ecommerce.exceptions.RecordNotFoundException;
import com.happy.ecommerce.repositories.OrderRepository;
import com.happy.ecommerce.repositories.ProductRepository;
import com.happy.ecommerce.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        String errorMessage = String.format("User with id %d not found", orderRequest.getUserId());
        User user = userRepository.findById(orderRequest.getUserId()).
                orElseThrow(() -> new RecordNotFoundException(errorMessage));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = createOrderItems(orderRequest.getProductList(), savedOrder);
        savedOrder.setOrderItems(orderItems);

        double totalAmount = calculateTotalAmount(orderItems);
        savedOrder.setTotalAmount(totalAmount);

        savedOrder = orderRepository.save(savedOrder);
        return mapOrderToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapOrderToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrder(int orderId) {
        String errorMessage = String.format("Order with id %d not found", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RecordNotFoundException(errorMessage));

        return mapOrderToResponse(order);
    }

    private double calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    private List<OrderItem> createOrderItems(List<OrderRequest.ProductItem> orderRequests, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderRequest.ProductItem orderProductItem : orderRequests) {
            int productId = orderProductItem.getProductId();
            int quantity = orderProductItem.getQuantity();

            String errorMessage = String.format("Product not found with id %d", productId);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RecordNotFoundException(errorMessage));

            OrderItem orderItem = OrderItem.builder()
                    .quantity(quantity)
                    .subtotal(product.getPrice() * quantity)
                    .product(product)
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private OrderResponse mapOrderToResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(Math.toIntExact(order.getId()));
        orderResponse.setUserId(Math.toIntExact(order.getUser().getId()));
        orderResponse.setOrderDate(order.getOrderDate().toString());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setOrderItemList(mapOrderItemsToResponse(order.getOrderItems()));
        return orderResponse;
    }

    private List<OrderResponse.OrderItem> mapOrderItemsToResponse(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> OrderResponse.OrderItem.builder()
                        .productId(Math.toIntExact(orderItem.getProduct().getId()))
                        .quantity(orderItem.getQuantity())
                        .subtotal(orderItem.getSubtotal())
                        .build())
                .collect(Collectors.toList());
    }
}
