package com.pablo.digitalstore.digital_store_api.repository;

import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserProductEntity;
import com.pablo.digitalstore.digital_store_api.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductRepository extends JpaRepository<UserProductEntity, Long> {
    List<UserProductEntity> findByUser(UserEntity user);
    Page<UserProductEntity> findByUserAndOrderStatus(UserEntity user, OrderStatus status, Pageable pageable);

}
