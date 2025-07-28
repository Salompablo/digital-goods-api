package com.pablo.digitalstore.digital_store_api.exception;

import java.io.Serializable;

public class UserDisabledException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public UserDisabledException(String message) {
        super(message);
    }

    public UserDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
