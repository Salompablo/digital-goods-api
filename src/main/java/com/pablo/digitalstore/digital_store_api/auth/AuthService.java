package com.pablo.digitalstore.digital_store_api.auth;

import com.pablo.digitalstore.digital_store_api.model.dto.request.AuthRefreshRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.AuthRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.UserRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(UserRequest request);
    AuthResponse refreshToken(AuthRefreshRequest request);
}
