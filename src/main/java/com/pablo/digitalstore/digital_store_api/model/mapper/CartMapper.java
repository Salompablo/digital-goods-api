package com.pablo.digitalstore.digital_store_api.model.mapper;


import com.pablo.digitalstore.digital_store_api.model.dto.response.CartItemResponse;
import com.pablo.digitalstore.digital_store_api.model.dto.response.CartResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.CartEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.CartItemEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toItemResponse(CartItemEntity item) {
        BigDecimal unitPrice = item.getUnitPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        return CartItemResponse.builder()
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .unitPrice(unitPrice)
                .quantity(item.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }

    public CartResponse toCartResponse(CartEntity cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .items(items)
                .totalAmount(total)
                .build();
    }
}
