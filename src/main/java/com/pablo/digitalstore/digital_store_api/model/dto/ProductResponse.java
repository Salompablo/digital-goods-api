package com.pablo.digitalstore.digital_store_api.model.dto;

import com.pablo.digitalstore.digital_store_api.model.enums.ProductType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private ProductType type;

    private Boolean premiumOnly;

    private String fileUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
