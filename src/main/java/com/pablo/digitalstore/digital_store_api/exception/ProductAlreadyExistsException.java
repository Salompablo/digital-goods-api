package com.pablo.digitalstore.digital_store_api.exception;

import java.io.Serializable;

public class ProductAlreadyExistsException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public ProductAlreadyExistsException(String message) {
        super(message);
    }

    public ProductAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
