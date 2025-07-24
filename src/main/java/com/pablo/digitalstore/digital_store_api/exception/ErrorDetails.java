package com.pablo.digitalstore.digital_store_api.exception;

import lombok.*;

import java.sql.Timestamp;

public record ErrorDetails(
        Timestamp date,
        String message,
        String details
) {
    public static ErrorDetails from(String message, String details) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(0);

        return new ErrorDetails(timestamp, message, details);
    }
}
