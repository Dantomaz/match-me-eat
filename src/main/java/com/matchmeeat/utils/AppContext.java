package com.matchmeeat.utils;

import com.matchmeeat.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppContext {

    public static Optional<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User user)) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    public static String getUsername() {
        return getUser().map(User::getUsername).orElse(DynamicAuditorContext.SYSTEM_AUDITOR);
    }
}
