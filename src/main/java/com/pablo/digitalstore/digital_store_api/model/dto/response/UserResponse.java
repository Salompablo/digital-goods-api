package com.pablo.digitalstore.digital_store_api.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder

@Schema(description = "User details returned after authentication or in user profile.")
public record UserResponse (
    @Schema(description = "User's first name", example = "John")
    String firstName,

    @Schema(description = "User's last name", example = "Doe")
    String lastName,

    @Schema(description = "User's email address", example = "john.doe@example.com")
    String email,

    @Schema(description = "Whether the user account is active", example = "true")
    Boolean active
) {}
