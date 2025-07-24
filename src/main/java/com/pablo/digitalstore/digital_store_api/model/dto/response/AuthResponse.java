package com.pablo.digitalstore.digital_store_api.model.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}
