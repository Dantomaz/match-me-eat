package com.matchmeeat.auth.service;

import com.matchmeeat.role.entity.Role;
import com.matchmeeat.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class JwtService {

    public static final String CLAIM_ROLES = "roles";

    private final String issuer;
    private final Duration ttl;
    private final JwtEncoder jwtEncoder;
    private final RoleService roleService;

    public String generateToken(String username) {
        List<String> roles = roleService.findUserRoles(username).stream().map(Role::getAuthority).toList();

        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
            .subject(username)
            .issuer(issuer)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(ttl))
            .claim(CLAIM_ROLES, roles)
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
