package com.pablo.digitalstore.digital_store_api.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to request a new access token using a refresh token")
public record AuthRefreshRequest(

        @NotBlank(message = "Refresh token is required.")
        @Schema(description = "Refresh token issued during login", example = "eyJhbGciOiJIUzI1NiIsInR...")
        String refreshToken

) {}
