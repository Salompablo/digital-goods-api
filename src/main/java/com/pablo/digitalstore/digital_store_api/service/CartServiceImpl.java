package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.request.AddProductToCartRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.CartItemResponse;
import com.pablo.digitalstore.digital_store_api.model.dto.response.CartResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.CartEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.CartItemEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.ProductEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.model.mapper.CartMapper;
import com.pablo.digitalstore.digital_store_api.repository.CartItemRepository;
import com.pablo.digitalstore.digital_store_api.repository.CartRepository;
import com.pablo.digitalstore.digital_store_api.repository.ProductRepository;
import com.pablo.digitalstore.digital_store_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public CartResponse addProductToCart(Long userId, AddProductToCartRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ProductEntity product = productRepository.findById(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CartEntity cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(user);
                    newCart.setCreatedAt(LocalDateTime.now());
                    return newCart;
                });

        cart.setUpdatedAt(LocalDateTime.now());

        CartItemEntity item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> CartItemEntity.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(0)
                        .unitPrice(product.getPrice())
                        .build());

        item.setQuantity(item.getQuantity() + request.quantity());

        cart.getItems().removeIf(i -> i.getProduct().getProductId().equals(product.getProductId()));
        cart.getItems().add(item);

        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }
}
