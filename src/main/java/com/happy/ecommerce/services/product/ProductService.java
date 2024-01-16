package com.happy.ecommerce.services.product;

import com.happy.ecommerce.dto.product.ProductRequest;
import com.happy.ecommerce.dto.product.ProductResponse;

import java.util.List;
public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    List<ProductResponse> getProducts();
    ProductResponse getProduct(int productId);
}
