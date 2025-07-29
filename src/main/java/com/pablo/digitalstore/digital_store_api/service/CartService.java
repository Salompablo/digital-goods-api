package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.request.AddProductToCartRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.CartResponse;

public interface CartService {
    CartResponse addProductToCart(Long userId, AddProductToCartRequest request);
}
