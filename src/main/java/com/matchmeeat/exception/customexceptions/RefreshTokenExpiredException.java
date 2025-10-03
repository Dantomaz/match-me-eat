package com.matchmeeat.exception.customexceptions;

import org.springframework.security.core.AuthenticationException;

public class RefreshTokenExpiredException extends AuthenticationException {

    public RefreshTokenExpiredException(String msg) {
        super(msg);
    }
}
