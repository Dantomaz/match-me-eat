package com.matchmeeat.auth.token.entity;

import com.matchmeeat.base.entity.BaseEntity;
import com.matchmeeat.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = RefreshToken.TABLE_NAME)
public class RefreshToken extends BaseEntity {

    public static final String TABLE_NAME = "refresh_tokens";

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean revoked = false;

    private RefreshToken(String token, User user, Instant expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public static RefreshToken create(User user, Duration ttl) {
        return new RefreshToken(UUID.randomUUID().toString(), user, Instant.now().plus(ttl));
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public void revoke() {
        revoked = true;
    }
}
