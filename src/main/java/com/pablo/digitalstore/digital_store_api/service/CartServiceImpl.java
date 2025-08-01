package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.request.AddProductToCartRequest;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           CartMapper cartMapper,
                           UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
        this.userService = userService;
    }

    @Override
    public CartResponse getCurrentCart() {
        UserEntity user = userService.getCurrentUserEntity();

        CartEntity cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    CartEntity newCart = CartEntity.builder()
                            .user(user)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse addProductToCurrentCart(AddProductToCartRequest request) {
        UserEntity user = userService.getCurrentUserEntity();
        ProductEntity product = getProductOrThrow(request.productId());

        CartEntity cart = cartRepository.findByUser(user).orElse(null);

        if (cart == null) {
            cart = CartEntity.builder()
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            cart = cartRepository.save(cart);
        } else {
            cart.setUpdatedAt(LocalDateTime.now());
        }

        CartItemEntity item = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        if (item == null) {
            item = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(0)
                    .unitPrice(product.getPrice())
                    .build();
        }

        item.setQuantity(item.getQuantity() + request.quantity());

        cart.getItems().removeIf(i -> i.getProduct().getProductId().equals(product.getProductId()));
        cart.getItems().add(item);

        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse decreaseProductQuantityInCurrentCart(Long productId) {
        UserEntity user = userService.getCurrentUserEntity();
        ProductEntity product = getProductOrThrow(productId);
        CartEntity cart = getCartOrThrow(user);
        CartItemEntity item = getCartItemOrThrow(cart, product);

        int newQuantity = item.getQuantity() - 1;

        if (newQuantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQuantity);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse removeProductFromCurrentCart(Long productId) {
        UserEntity user = userService.getCurrentUserEntity();
        ProductEntity product = getProductOrThrow(productId);
        CartEntity cart = getCartOrThrow(user);
        CartItemEntity item = getCartItemOrThrow(cart, product);

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse clearCurrentCart() {
        UserEntity user = userService.getCurrentUserEntity();
        CartEntity cart = getCartOrThrow(user);

        for (CartItemEntity item : new ArrayList<>(cart.getItems())) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    private ProductEntity getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    private CartEntity getCartOrThrow(UserEntity user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    private CartItemEntity getCartItemOrThrow(CartEntity cart, ProductEntity product) {
        return cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new EntityNotFoundException("Product not in cart"));
    }
}
