package com.sawah.sawah_backend.exceptions;

public class ProviderNotApprovedException extends RuntimeException {
    public ProviderNotApprovedException(String message) {
        super(message);
    }
}
