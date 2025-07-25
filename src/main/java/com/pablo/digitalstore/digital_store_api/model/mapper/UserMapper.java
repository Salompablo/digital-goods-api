package com.pablo.digitalstore.digital_store_api.model.mapper;

import com.pablo.digitalstore.digital_store_api.model.dto.request.UserRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.UserResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserRequest request) {
        return UserEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .active(true)
                .build();
    }

    public UserResponse toResponse(UserEntity entity) {
        return UserResponse.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getCredentials().getEmail())
                .active(entity.getActive())
                .build();
    }
}
