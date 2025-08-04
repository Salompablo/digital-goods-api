package com.pablo.digitalstore.digital_store_api.repository;

import com.pablo.digitalstore.digital_store_api.model.entity.OrderEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findByUser(UserEntity user, Pageable pageable);
    Optional<OrderEntity> findByUserAndStatus(UserEntity user, OrderStatus status);
    boolean existsByUserAndStatus(UserEntity user, OrderStatus status);
}
