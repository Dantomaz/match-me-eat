package com.matchmeeat.configuration;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Jwt credentials;

    protected CustomAuthenticationToken(Object principal, Jwt token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = token;
        super.setAuthenticated(true);
    }
}
