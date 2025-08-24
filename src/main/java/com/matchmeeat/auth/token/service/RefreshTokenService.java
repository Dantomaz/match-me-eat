package com.matchmeeat.auth.token.service;

import com.matchmeeat.auth.token.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);
}
