package com.pablo.digitalstore.digital_store_api.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for user registration",
        requiredProperties = {"firstName", "lastName", "email", "password"})
public record UserRequest(

        @NotBlank(message = "First name is required.")
        @Size(max = 50, message = "First name must be at most 50 characters.")
        @Schema(description = "User's first name", example = "Pablo")
        String firstName,

        @NotBlank(message = "Last name is required.")
        @Size(max = 50, message = "Last name must be at most 50 characters.")
        @Schema(description = "User's last name", example = "Salom")
        String lastName,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Schema(description = "User's email", example = "pablo.salom@example.com")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
        @Schema(description = "User's password", example = "StrongPassword123")
        String password

) {}
