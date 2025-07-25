package com.pablo.digitalstore.digital_store_api.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Response returned after successful authentication, containing access and refresh tokens."
)
public record AuthResponse(
        @Schema(
                description = "JWT access token used to authenticate subsequent API requests",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String accessToken,

        @Schema(
                description = "JWT refresh token used to obtain a new access token when the current one expires",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String refreshToken
) {}
