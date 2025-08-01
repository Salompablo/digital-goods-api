package com.pablo.digitalstore.digital_store_api.model.dto.response;

import com.pablo.digitalstore.digital_store_api.model.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Details of a completed order")
public record OrderResponse(
        @Schema(description = "Unique identifier for the order", example = "2001")
        Long orderId,

        @Schema(description = "Date and time the order was created", example = "2025-08-01T14:32:00")
        LocalDateTime createdAt,

        @Schema(description = "Status of the order", example = "PENDING")
        OrderStatus status,

        @Schema(description = "List of items included in the order")
        List<OrderItemResponse> items
) {
}
