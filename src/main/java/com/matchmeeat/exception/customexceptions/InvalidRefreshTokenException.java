package com.matchmeeat.exception.customexceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidRefreshTokenException extends AuthenticationException {

    public InvalidRefreshTokenException(String msg) {
        super(msg);
    }
}
