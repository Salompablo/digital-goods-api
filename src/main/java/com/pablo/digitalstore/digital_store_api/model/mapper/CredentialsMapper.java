package com.pablo.digitalstore.digital_store_api.model.mapper;

import com.pablo.digitalstore.digital_store_api.model.entity.CredentialsEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.RoleEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.model.enums.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CredentialsMapper {


    public CredentialsEntity toEntity(String email, String rawPassword, UserEntity user, RoleEntity defaultRole) {
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(defaultRole);
        
        return CredentialsEntity.builder()
                .email(email)
                .password(rawPassword)
                .user(user)
                .authProvider(AuthProvider.LOCAL)
                .roles(roles)
                .build();
    }
}
