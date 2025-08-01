package com.pablo.digitalstore.digital_store_api.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Representation of an item within an order")
public record OrderItemResponse(
        @Schema(description = "ID of the purchased product", example = "101")
        Long productId,

        @Schema(description = "Name of the product", example = "Java Masterclass")
        String productName,

        @Schema(description = "Quantity purchased", example = "2")
        Integer quantity,

        @Schema(description = "Unit price at the time of purchase", example = "49.99")
        BigDecimal unitPrice
) {
}
