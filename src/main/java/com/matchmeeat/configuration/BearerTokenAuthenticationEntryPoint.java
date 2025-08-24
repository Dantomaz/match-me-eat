package com.matchmeeat.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchmeeat.exception.CustomProblemDetail;
import com.matchmeeat.exception.ProblemTypeResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@Slf4j
@Component
public class BearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        log.info("{}: {}", httpStatus.getReasonPhrase(), authException.getMessage());

        ProblemDetail problemDetail = CustomProblemDetail.builder()
            .type(URI.create(ProblemTypeResolver.resolve(authException.getClass())))
            .httpStatus(httpStatus)
            .title(httpStatus.getReasonPhrase())
            .detail(authException.getMessage())
            .instance(URI.create(request.getRequestURI()))
            .build();

        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), problemDetail);
    }
}
