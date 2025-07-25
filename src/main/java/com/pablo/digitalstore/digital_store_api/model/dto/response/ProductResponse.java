package com.pablo.digitalstore.digital_store_api.model.dto.response;

import com.pablo.digitalstore.digital_store_api.model.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder

@Schema(description = "Response object containing detailed information about a digital product.")
public record ProductResponse(

        @Schema(description = "Unique identifier of the product", example = "1")
        Long id,

        @Schema(description = "Name of the product", example = "Java Masterclass")
        String name,

        @Schema(description = "Detailed description of the product", example = "Learn Java from basics to advanced with hands-on projects.")
        String description,

        @Schema(description = "Price of the product in USD", example = "49.99")
        BigDecimal price,

        @Schema(description = "Type of the product", example = "EBOOK")
        ProductType type,

        @Schema(description = "Whether the product is only available to premium users", example = "false")
        Boolean premiumOnly,

        @Schema(description = "Download URL of the product file", example = "https://cdn.digitalstore.com/products/java-course.zip")
        String fileUrl,

        @Schema(description = "Date and time when the product was created", example = "2025-07-24T18:12:30")
        LocalDateTime createdAt,

        @Schema(description = "Date and time when the product was last updated", example = "2025-07-24T18:15:45")
        LocalDateTime updatedAt

) {}
