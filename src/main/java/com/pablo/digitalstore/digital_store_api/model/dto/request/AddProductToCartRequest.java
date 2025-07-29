package com.pablo.digitalstore.digital_store_api.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder

@Schema(description = "Request to add a product to the user's shopping cart")
public record AddProductToCartRequest(

        @Schema(description = "ID of the product to be added", example = "1", required = true)
        Long productId,

        @Schema(description = "Quantity of the product to add", example = "2", required = true)
        Integer quantity
) {}
