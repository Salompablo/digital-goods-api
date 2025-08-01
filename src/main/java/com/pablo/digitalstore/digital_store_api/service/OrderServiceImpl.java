package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.response.OrderResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.*;
import com.pablo.digitalstore.digital_store_api.model.enums.OrderStatus;
import com.pablo.digitalstore.digital_store_api.model.mapper.OrderMapper;
import com.pablo.digitalstore.digital_store_api.repository.CartRepository;
import com.pablo.digitalstore.digital_store_api.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements  OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            UserService userService,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponse checkoutCurrentCart() {
        UserEntity user = userService.getCurrentUserEntity();

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

}
