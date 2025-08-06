package com.pablo.digitalstore.digital_store_api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

        @Schema(description = "Download URL of the product file", example = "https://cdn.digitalstore.com/products/java-course.zip")
        String fileUrl,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "Date and time when the product was created", example = "2025-07-24T18:12:30")
        LocalDateTime createdAt,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "Date and time when the product was last updated", example = "2025-07-24T18:15:45")
        LocalDateTime updatedAt

) {}
