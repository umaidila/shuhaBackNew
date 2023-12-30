package ru.shuha.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginException extends AuthenticationException {

    public InvalidLoginException(String message) {
        super(message);
    }

}
