package com.pablo.digitalstore.digital_store_api.model.mapper;

import com.pablo.digitalstore.digital_store_api.model.dto.request.UserRequest;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserRequest request) {
        return UserEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }
}
