package com.pablo.digitalstore.digital_store_api.model.dto;

import com.pablo.digitalstore.digital_store_api.model.enums.ProductType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Request object for creating or updating a digital product",
        requiredProperties = {"name", "description", "price", "type"}
)
public class ProductRequest {

    @NotBlank(message = "Product name cannot be empty.")
    @Size(max = 100, message = "Product name must be at most 100 characters.")
    @Schema(description = "Name of the product", example = "Ultimate Java Course")
    private String name;

    @NotBlank(message = "Product description cannot be empty.")
    @Schema(description = "Detailed description of the product", example = "A complete course to master Java 21.")
    private String description;

    @NotNull(message = "Product price is required.")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0 with 2 decimal precision.")
    @Pattern(regexp = "^(\\d+\\.\\d{1,2})?$", message = "Price must have at most two decimal places.")
    @Schema(description = "Price of the product", example = "19.99")
    private BigDecimal price;

    @NotNull(message = "Product type is required.")
    @Schema(description = "Type of product", example = "COURSE")
    private ProductType type;

    @NotNull(message = "Premium flag is required.")
    @Schema(description = "Indicates if the product requires a premium subscription", example = "true")
    private Boolean premiumOnly;

    @Pattern(regexp = "^(http|https)://.*", message = "Invalid URL format. URL should start with http:// or https://")
    @Size(max = 255, message = "File URL must be at most 255 characters.")
    @Schema(description = "URL or download link of the product file", example = "https://cdn.digitalstore.com/products/java-course.zip")
    private String fileUrl;

}
