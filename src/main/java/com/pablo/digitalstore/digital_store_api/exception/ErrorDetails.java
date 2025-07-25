package com.pablo.digitalstore.digital_store_api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;

@Schema(description = "Details of the error returned by the API")
public record ErrorDetails(

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "Date and time when the error occurred", example = "2025-07-25T18:12:34")
        Timestamp date,

        @Schema(description = "Error message", example = "Unauthorized access")
        String message,

        @Schema(description = "Additional error details", example = "/api/v1/users/me")
        String details
) {
    public static ErrorDetails from(String message, String details) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(0);

        return new ErrorDetails(timestamp, message, details);
    }
}
