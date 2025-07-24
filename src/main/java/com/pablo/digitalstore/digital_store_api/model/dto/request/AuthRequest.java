package com.pablo.digitalstore.digital_store_api.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for user authentication",
        requiredProperties = {"email", "password"})
public record AuthRequest(

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Schema(description = "Email of the user", example = "user@example.com")
        String email,

        @NotBlank(message = "Password is required.")
        @Schema(description = "Password of the user", example = "password123")
        String password
) {}
