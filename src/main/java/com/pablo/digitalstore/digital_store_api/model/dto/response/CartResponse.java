package com.pablo.digitalstore.digital_store_api.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder

@Schema(description = "Response containing the user's current shopping cart")
public record CartResponse(

        @Schema(description = "List of items in the cart")
        List<CartItemResponse> items,

        @Schema(description = "Total cost of all items in the cart", example = "199.99")
        BigDecimal totalAmount

) {}
