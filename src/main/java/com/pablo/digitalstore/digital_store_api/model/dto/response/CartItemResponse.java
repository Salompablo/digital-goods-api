package com.pablo.digitalstore.digital_store_api.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder

@Schema(description = "Represents an item in the shopping cart")
public record CartItemResponse(

        @Schema(description = "Product ID", example = "1")
        Long productId,

        @Schema(description = "Name of the product", example = "Advanced Java Course")
        String productName,

        @Schema(description = "Unit price of the product", example = "49.99")
        BigDecimal unitPrice,

        @Schema(description = "Quantity of this product in the cart", example = "2")
        Integer quantity,

        @Schema(description = "Total price for this product (unitPrice * quantity)", example = "99.98")
        BigDecimal totalPrice

) {}
