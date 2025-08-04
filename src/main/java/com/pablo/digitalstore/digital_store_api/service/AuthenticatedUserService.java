package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.entity.CredentialsEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.repository.CredentialsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserService {

    private final CredentialsRepository credentialsRepository;

    public AuthenticatedUserService(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    public CredentialsEntity getCurrentCredentials() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserEntity getCurrentUserEntity() {
        return getCurrentCredentials().getUser();
    }
}
