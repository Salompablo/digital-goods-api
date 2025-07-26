package com.pablo.digitalstore.digital_store_api.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for changing the current user's password")
public record ChangePasswordRequest(
        @NotBlank(message = "Current password is required")
        @Schema(example = "MyOldPassword123")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters")
        @Schema(example = "NewSecurePassword123")
        String newPassword,

        @NotBlank(message = "Password confirmation is required")
        @Schema(example = "NewSecurePassword123")
        String confirmPassword
) {}
