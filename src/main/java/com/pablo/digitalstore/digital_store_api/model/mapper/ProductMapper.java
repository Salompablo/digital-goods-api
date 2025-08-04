package com.pablo.digitalstore.digital_store_api.model.mapper;

import com.pablo.digitalstore.digital_store_api.model.dto.request.ProductRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.ProductResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity toEntity(ProductRequest productRequest){
        return ProductEntity.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .type(productRequest.getType())
                .fileUrl(productRequest.getFileUrl())
                .build();
    }

    public ProductResponse toResponse(ProductEntity productEntity){
        return ProductResponse.builder()
                .id(productEntity.getProductId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .type(productEntity.getType())
                .fileUrl(productEntity.getFileUrl())
                .createdAt(productEntity.getCreatedAt())
                .updatedAt(productEntity.getUpdatedAt())
                .build();
    }

    public Page<ProductResponse> toResponsePage(Page<ProductEntity> productEntityPage){
        return productEntityPage.map(this::toResponse);
    }

}
