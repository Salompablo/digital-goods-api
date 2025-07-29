package com.pablo.digitalstore.digital_store_api.repository;

import com.pablo.digitalstore.digital_store_api.model.entity.CartEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.CartItemEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    Optional<CartItemEntity> findByCartAndProduct(CartEntity cart, ProductEntity product);
}
