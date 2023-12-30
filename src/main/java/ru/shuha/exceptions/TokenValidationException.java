package ru.shuha.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenValidationException extends AuthenticationException {

    public TokenValidationException(String message) {
        super(message);
    }

}
