package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.request.AddProductToCartRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.CartResponse;

public interface CartService {
    CartResponse getCurrentCart();
    CartResponse addProductToCurrentCart(AddProductToCartRequest request);
    CartResponse decreaseProductQuantityInCurrentCart(Long productId);
    CartResponse removeProductFromCurrentCart(Long productId);
    CartResponse clearCurrentCart();
}
