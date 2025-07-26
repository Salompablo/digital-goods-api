package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.model.dto.request.ChangePasswordRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.UserUpdateRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse getCurrentUser();
    UserResponse updateCurrentUser(UserUpdateRequest request);
    void deactivateCurrentUser();
    void reactivateCurrentUser();
    void changeMyPassword(ChangePasswordRequest request);

    Page<UserResponse> findAllUsers(Pageable pageable);
    UserResponse findUserById(Long userId);
    UserResponse updateUserById(Long userId, UserUpdateRequest request);
    void deactivateUserById(Long userId);
    void reactivateUserById(Long userId);
}
