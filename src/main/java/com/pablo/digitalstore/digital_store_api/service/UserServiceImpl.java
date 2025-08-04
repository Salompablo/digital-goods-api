package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.request.ChangePasswordRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.UserUpdateRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.ProductResponse;
import com.pablo.digitalstore.digital_store_api.model.dto.response.UserResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.CredentialsEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserProductEntity;
import com.pablo.digitalstore.digital_store_api.model.mapper.ProductMapper;
import com.pablo.digitalstore.digital_store_api.model.mapper.UserMapper;
import com.pablo.digitalstore.digital_store_api.repository.CredentialsRepository;
import com.pablo.digitalstore.digital_store_api.repository.ProductRepository;
import com.pablo.digitalstore.digital_store_api.repository.UserProductRepository;
import com.pablo.digitalstore.digital_store_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CredentialsRepository credentialsRepository;
    private final UserMapper userMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final ProductMapper productMapper;
    private final UserProductRepository userProductRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           CredentialsRepository credentialsRepository,
                           UserMapper userMapper,
                           AuthenticatedUserService authenticatedUserService,
                           ProductMapper productMapper,
                           UserProductRepository userProductRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
        this.userMapper = userMapper;
        this.authenticatedUserService = authenticatedUserService;
        this.productMapper = productMapper;
        this.userProductRepository = userProductRepository;
    }

    @Override
    public UserResponse getCurrentUser() {
        UserEntity user = authenticatedUserService.getCurrentUserEntity();
        return userMapper.toResponse(user);
    }

    @Override
    public UserEntity getCurrentUserEntity() {
        return authenticatedUserService.getCurrentUserEntity();
    }

    @Override
    public UserResponse updateCurrentUser(UserUpdateRequest request) {
        UserEntity user = authenticatedUserService.getCurrentUserEntity();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void deactivateCurrentUser() {
        UserEntity user = authenticatedUserService.getCurrentUserEntity();

        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void changeMyPassword(ChangePasswordRequest request) {
        CredentialsEntity credentials = credentialsRepository.findByUser(authenticatedUserService.getCurrentUserEntity())
                .orElseThrow(() -> new UsernameNotFoundException("User credentials not found"));

        if (!passwordEncoder.matches(request.currentPassword(), credentials.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        credentials.setPassword(passwordEncoder.encode(request.newPassword()));
        credentialsRepository.save(credentials);
    }

    @Override
    public Page<UserResponse> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    public UserResponse findUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserById(Long userId, UserUpdateRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void deactivateUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void reactivateUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void reactivateCurrentUser() {
        UserEntity user = authenticatedUserService.getCurrentUserEntity();

        if (Boolean.TRUE.equals(user.getActive())) {
            throw new IllegalStateException("User account is already active.");
        }

        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public List<ProductResponse> getPurchasedProductsForCurrentUser() {
        UserEntity user = authenticatedUserService.getCurrentUserEntity();
        List<UserProductEntity> userProducts = userProductRepository.findByUser(user);

        return userProducts.stream()
                .map(UserProductEntity::getProduct)
                .map(productMapper::toResponse)
                .toList();
    }

}

