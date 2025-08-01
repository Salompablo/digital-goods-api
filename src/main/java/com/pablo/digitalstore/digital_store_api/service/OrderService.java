package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse checkoutCurrentCart();
    Page<OrderResponse> getOrdersByCurrentUser(Pageable pageable);
    void cancelCurrentCart();
}
