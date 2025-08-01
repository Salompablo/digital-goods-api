package com.pablo.digitalstore.digital_store_api.model.mapper;

import com.pablo.digitalstore.digital_store_api.model.dto.response.OrderItemResponse;
import com.pablo.digitalstore.digital_store_api.model.dto.response.OrderResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.OrderEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.OrderItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderItemResponse toOrderItemResponse(OrderItemEntity orderItem) {
        return new OrderItemResponse(
                orderItem.getProduct().getProductId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }

    public List<OrderItemResponse> toOrderItemResponseList(List<OrderItemEntity> items) {
        return items.stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse toOrderResponse(OrderEntity order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCreatedAt(),
                order.getStatus(),
                toOrderItemResponseList(order.getItems())
        );
    }

    public Page<OrderResponse> toResponsePage(Page<OrderEntity> orderEntityPage) {
        return orderEntityPage.map(this::toOrderResponse);
    }

}
