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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@Slf4j
@Component
public class BearerTokenAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        log.info("{}: {}", httpStatus.getReasonPhrase(), accessDeniedException.getMessage());

        ProblemDetail problemDetail = CustomProblemDetail.builder()
            .type(URI.create(ProblemTypeResolver.resolve(accessDeniedException.getClass())))
            .httpStatus(httpStatus)
            .title(httpStatus.getReasonPhrase())
            .detail(accessDeniedException.getMessage())
            .instance(URI.create(request.getRequestURI()))
            .build();

        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), problemDetail);
    }
}
