package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.exception.BusinessException;
import com.pablo.digitalstore.digital_store_api.model.dto.response.OrderResponse;
import com.pablo.digitalstore.digital_store_api.model.dto.response.ProductResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.*;
import com.pablo.digitalstore.digital_store_api.model.enums.OrderStatus;
import com.pablo.digitalstore.digital_store_api.model.mapper.OrderMapper;
import com.pablo.digitalstore.digital_store_api.model.mapper.ProductMapper;
import com.pablo.digitalstore.digital_store_api.repository.CartRepository;
import com.pablo.digitalstore.digital_store_api.repository.OrderRepository;
import com.pablo.digitalstore.digital_store_api.repository.UserProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements  OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final UserProductRepository userProductRepository;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            UserService userService,
                            OrderMapper orderMapper, UserProductRepository userProductRepository, ProductMapper productMapper) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.orderMapper = orderMapper;
        this.userProductRepository = userProductRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public OrderResponse checkoutCurrentCart() {
        UserEntity user = userService.getCurrentUserEntity();

        if (orderRepository.existsByUserAndStatus(user, OrderStatus.PENDING)) {
            throw new BusinessException("You already have a pending order. Please complete or cancel it first.");
        }

        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout with empty cart");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        for (CartItemEntity cartItem : cart.getItems()) {
            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getUnitPrice())
                    .build();

            order.getItems().add(orderItem);
        }

        for (OrderItemEntity item : order.getItems()) {
            UserProductEntity userProduct = UserProductEntity.builder()
                    .user(user)
                    .product(item.getProduct())
                    .order(order)
                    .purchaseDate(LocalDateTime.now())
                    .build();

            userProductRepository.save(userProduct);
        }

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getOrdersByCurrentUser(Pageable pageable) {
        UserEntity user = userService.getCurrentUserEntity();
        Page<OrderEntity> ordersPage = orderRepository.findByUser(user, pageable);
        return orderMapper.toResponsePage(ordersPage);
    }

    @Transactional
    public void cancelCurrentCart() {
        UserEntity user = userService.getCurrentUserEntity();

        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public OrderResponse getPendingOrderForCurrentUser() {
        UserEntity user = userService.getCurrentUserEntity();

        OrderEntity pendingOrder = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("No pending order found"));

        return orderMapper.toOrderResponse(pendingOrder);
    }

    @Override
    public Page<ProductResponse> getProductsForCurrentUserByOrderStatus(OrderStatus status, Pageable pageable) {
        UserEntity user = userService.getCurrentUserEntity();
        Page<UserProductEntity> userProducts = userProductRepository.findByUserAndOrderStatus(user, status, pageable);

        return userProducts.map(userProduct -> productMapper.toResponse(userProduct.getProduct()));
    }
}
