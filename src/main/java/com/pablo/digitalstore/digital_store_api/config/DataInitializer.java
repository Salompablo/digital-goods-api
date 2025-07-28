package com.pablo.digitalstore.digital_store_api.config;

import com.pablo.digitalstore.digital_store_api.model.entity.CredentialsEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.RoleEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.model.enums.AuthProvider;
import com.pablo.digitalstore.digital_store_api.model.enums.Roles;
import com.pablo.digitalstore.digital_store_api.repository.CredentialsRepository;
import com.pablo.digitalstore.digital_store_api.repository.RoleRepository;
import com.pablo.digitalstore.digital_store_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(
            UserRepository userRepository,
            CredentialsRepository credentialsRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_EMAIL}") String adminEmail,
            @Value("${ADMIN_PASSWORD}") String adminPassword
    ) {
        return args -> {
            if (!credentialsRepository.existsByEmail("admin@admin.com")) {

                UserEntity user = new UserEntity();
                user.setFirstName("Admin");
                user.setLastName("User");
                user.setActive(true);
                user = userRepository.save(user);

                CredentialsEntity credentials = new CredentialsEntity();
                credentials.setEmail(adminEmail);
                credentials.setPassword(passwordEncoder.encode(adminPassword));
                credentials.setAuthProvider(AuthProvider.LOCAL);
                credentials.setUser(user);

                RoleEntity adminRole = roleRepository.findByRole(Roles.valueOf("ROLE_ADMIN"))
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                credentials.setRoles(Set.of(adminRole));
                credentialsRepository.save(credentials);
            }
        };
    }
}
