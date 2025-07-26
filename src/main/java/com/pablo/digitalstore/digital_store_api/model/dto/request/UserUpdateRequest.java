package com.pablo.digitalstore.digital_store_api.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder

@Schema(description = "Payload for updating a user's personal profile")
public record UserUpdateRequest(
        @NotBlank(message = "First name is required")
        @Schema(example = "John")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Schema(example = "Doe")
        String lastName) {
}
