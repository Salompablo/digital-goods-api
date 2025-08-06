package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.response.OrderResponse;
import com.pablo.digitalstore.digital_store_api.model.dto.response.ProductResponse;
import com.pablo.digitalstore.digital_store_api.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponse checkoutCurrentCart();
    Page<OrderResponse> getOrdersByCurrentUser(Pageable pageable);
    void cancelCurrentCart();
    OrderResponse getPendingOrderForCurrentUser();
    Page<ProductResponse> getProductsForCurrentUserByOrderStatus(OrderStatus status, Pageable pageable);
}
