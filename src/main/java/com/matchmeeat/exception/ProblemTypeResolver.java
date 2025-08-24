package com.matchmeeat.exception;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.Map;

public final class ProblemTypeResolver {

    private static final String TYPE_PREFIX = "urn:problem-type:";
    private static final String DEFAULT_TYPE = "unknown";

    private ProblemTypeResolver() {
    }

    // Keep the entries sorted alphabetically by Exception class name to keep things readable
    private static final Map<Class<? extends Exception>, String> PROBLEM_TYPE_CATALOGUE = Map.ofEntries(
        Map.entry(HttpServerErrorException.InternalServerError.class, "internal-server-error"),
        Map.entry(IllegalArgumentException.class, "illegal-argument"),
        Map.entry(IOException.class, "io-error"),
        Map.entry(JsonGenerationException.class, "json-error"),
        Map.entry(JsonParseException.class, "json-error"),
        Map.entry(JsonProcessingException.class, "json-error"),
        Map.entry(MethodArgumentNotValidException.class, "request-parameters"),
        Map.entry(NumberFormatException.class, "number-format"),
        Map.entry(UsernameNotFoundException.class, "not-existing-user"),
        Map.entry(ValidationException.class, "validation-error")
    );

    public static String resolve(Class<? extends Exception> exceptionClass) {
        String resolvedType = PROBLEM_TYPE_CATALOGUE.get(exceptionClass);
        String finalType = StringUtils.isBlank(resolvedType) ? DEFAULT_TYPE : resolvedType;
        return TYPE_PREFIX + finalType;
    }
}
