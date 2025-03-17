package com.backend.pnta.Exceptions;

public class UserAlreadyAdminException extends RuntimeException {
    public UserAlreadyAdminException(String message) {
        super(message);
    }
}
