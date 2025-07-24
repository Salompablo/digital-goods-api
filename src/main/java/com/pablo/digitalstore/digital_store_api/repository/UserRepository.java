package com.pablo.digitalstore.digital_store_api.repository;

import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
