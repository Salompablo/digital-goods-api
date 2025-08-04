package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.request.ChangePasswordRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.UserUpdateRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.ProductResponse;
import com.pablo.digitalstore.digital_store_api.model.dto.response.UserResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse getCurrentUser();
    UserEntity getCurrentUserEntity();
    UserResponse updateCurrentUser(UserUpdateRequest request);
    void deactivateCurrentUser();
    void reactivateCurrentUser();
    void changeMyPassword(ChangePasswordRequest request);
    List<ProductResponse> getPurchasedProductsForCurrentUser();

    Page<UserResponse> findAllUsers(Pageable pageable);
    UserResponse findUserById(Long userId);
    UserResponse updateUserById(Long userId, UserUpdateRequest request);
    void deactivateUserById(Long userId);
    void reactivateUserById(Long userId);
}
