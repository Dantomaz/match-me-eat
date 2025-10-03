package com.matchmeeat.exception;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.matchmeeat.exception.customexceptions.EmailNotSentException;
import com.matchmeeat.exception.customexceptions.InvalidRefreshTokenException;
import com.matchmeeat.exception.customexceptions.RefreshTokenExpiredException;
import com.matchmeeat.exception.customexceptions.RefreshTokenRevokedException;
import com.matchmeeat.exception.customexceptions.UserRegistrationException;
import com.matchmeeat.exception.customexceptions.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProblemTypeResolver {

    private static final String TYPE_PREFIX = "urn:problem-type:";
    private static final String DEFAULT_TYPE = "unknown";

    // Keep the entries sorted alphabetically by Exception class name to keep things organized
    private static final Map<Class<? extends Exception>, String> PROBLEM_TYPE_CATALOGUE = Map.ofEntries(
        Map.entry(AccessDeniedException.class, "access-denied"),
        Map.entry(AuthenticationException.class, "authentication-failed"),
        Map.entry(HttpServerErrorException.InternalServerError.class, "internal-server-error"),
        Map.entry(IllegalArgumentException.class, "illegal-argument"),
        Map.entry(IOException.class, "io-error"),
        Map.entry(InvalidRefreshTokenException.class, "refresh-token-invalid"),
        Map.entry(JsonGenerationException.class, "json-error"),
        Map.entry(JsonParseException.class, "json-error"),
        Map.entry(JsonProcessingException.class, "json-error"),
        Map.entry(EmailNotSentException.class, "mail-not-sent"),
        Map.entry(MethodArgumentNotValidException.class, "request-parameters"),
        Map.entry(NumberFormatException.class, "number-format"),
        Map.entry(RefreshTokenExpiredException.class, "refresh-token-expired"),
        Map.entry(RefreshTokenRevokedException.class, "refresh-token-revoked"),
        Map.entry(UsernameNotFoundException.class, "not-existing-user"),
        Map.entry(UserRegistrationException.class, "user-registration-error"),
        Map.entry(ValidationException.class, "validation-error")
    );

    public static String resolve(Class<? extends Exception> exceptionClass) {
        String resolvedType = Optional.ofNullable(PROBLEM_TYPE_CATALOGUE.get(exceptionClass)) // Try to find perfect match with complexity of O(1)
            .orElseGet(() -> resolveBySuperclass(exceptionClass)); // If can not, try to find match by superclass with complexity of O(n)

        return TYPE_PREFIX + resolvedType;
    }

    private static String resolveBySuperclass(Class<? extends Exception> exceptionClass) {
        return PROBLEM_TYPE_CATALOGUE.entrySet().stream()
            .filter(entry -> entry.getKey().isAssignableFrom(exceptionClass))
            .findFirst()
            .map(Map.Entry::getValue)
            .orElse(DEFAULT_TYPE); // If it can not find any match then resolve to DEFAULT_TYPE
    }
}
