package com.pablo.digitalstore.digital_store_api.auth;

import com.pablo.digitalstore.digital_store_api.exception.ErrorDetails;
import com.pablo.digitalstore.digital_store_api.model.dto.request.AuthRefreshRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.AuthRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.UserRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication, registration, and token management")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Authenticate user and return JWT tokens",
            description = "Authenticates a user using email and password, returning a JWT access token and refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Register new user and return JWT tokens",
            description = "Registers a new user using first name, last name, email and password, and returns JWT tokens."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Registration successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Validation error or user already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid UserRequest userRequest) {
        AuthResponse response = authService.register(userRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Refresh JWT tokens",
            description = "Generates a new access and refresh token using a valid refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens refreshed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Valid AuthRefreshRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
