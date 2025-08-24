package com.matchmeeat.exception.customexceptions;

import org.springframework.security.core.AuthenticationException;

public class RefreshTokenRevokedException extends AuthenticationException {

    public RefreshTokenRevokedException(String msg) {
        super(msg);
    }
}
