package com.matchmeeat.configuration;

import com.matchmeeat.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserJwtAuthenticationConverter implements Converter<Jwt, CustomAuthenticationToken> {

    private final UserDetailsService userDetailsService;

    @SuppressWarnings("NullableProblems")
    @Override
    public CustomAuthenticationToken convert(Jwt token) {
        try {
            return convertFromUserDetails(token);
        } catch (Exception exception) {
            return convertFromJwtToken(token);
        }
    }

    private CustomAuthenticationToken convertFromUserDetails(Jwt token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getSubject());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return new CustomAuthenticationToken(userDetails, token, authorities);
    }

    /**
     * Fallback for standard JWT exception handling
     */
    private CustomAuthenticationToken convertFromJwtToken(Jwt token) {
        List<SimpleGrantedAuthority> authorities = token.getClaimAsStringList(JwtService.CLAIM_ROLES).stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
        return new CustomAuthenticationToken(token, token, authorities);
    }
}
