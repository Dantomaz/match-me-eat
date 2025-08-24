package com.matchmeeat.exception;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.matchmeeat.exception.customexceptions.InvalidRefreshTokenException;
import com.matchmeeat.exception.customexceptions.RefreshTokenExpiredException;
import com.matchmeeat.exception.customexceptions.RefreshTokenRevokedException;
import com.matchmeeat.exception.customexceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = exception.getBody();
        Map<String, List<String>> invalidParams = exception.getBindingResult()
            .getAllErrors()
            .stream()
            .collect(Collectors.groupingBy(
                error -> ((FieldError) error).getField(),
                Collectors.mapping(error -> Objects.requireNonNull(error.getDefaultMessage()), Collectors.toList())
            ));
        problemDetail.setProperty("invalid-params", invalidParams);
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler({ValidationException.class})
    protected ResponseEntity<ProblemDetail> handleValidation(ValidationException exception, ServletWebRequest request) {
        log.error(exception.getBody().getDetail());

        HttpStatus resolvedStatus = HttpStatus.resolve(exception.getStatusCode().value());
        //noinspection ConstantValue
        HttpStatus httpStatus = resolvedStatus != null ? resolvedStatus : HttpStatus.FORBIDDEN;

        ProblemDetail problemDetail = exception.getBody();
        return CustomProblemDetail.builder()
            .type(URI.create(ProblemTypeResolver.resolve(exception.getClass())))
            .httpStatus(httpStatus)
            .title(problemDetail.getTitle())
            .detail(problemDetail.getDetail())
            .instance(URI.create(request.getRequest().getRequestURI()))
            .properties(problemDetail.getProperties())
            .buildResponseEntity();
    }

    @ExceptionHandler({InvalidRefreshTokenException.class, RefreshTokenExpiredException.class, RefreshTokenRevokedException.class})
    protected ResponseEntity<ProblemDetail> handleUnauthorized(Exception exception, ServletWebRequest request) {
        return createProblemDetailResponseEntity(HttpStatus.UNAUTHORIZED, exception, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ProblemDetail> handleBadRequest(Exception exception, ServletWebRequest request) {
        return createProblemDetailResponseEntity(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler({HttpServerErrorException.InternalServerError.class, IOException.class})
    protected ResponseEntity<ProblemDetail> handleInternalServerError(Exception exception, ServletWebRequest request) {
        return createProblemDetailResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    @ExceptionHandler({JsonGenerationException.class, JsonParseException.class, JsonProcessingException.class, NumberFormatException.class})
    protected ResponseEntity<ProblemDetail> handleUnprocessableEntity(Exception exception, ServletWebRequest request) {
        return createProblemDetailResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ProblemDetail> handleNotFound(Exception exception, ServletWebRequest request) {
        return createProblemDetailResponseEntity(HttpStatus.NOT_FOUND, exception, request);
    }

    private ResponseEntity<ProblemDetail> createProblemDetailResponseEntity(HttpStatus httpStatus, Exception exception, ServletWebRequest request) {
        log.error(exception.getMessage());
        return CustomProblemDetail.builder()
            .type(URI.create(ProblemTypeResolver.resolve(exception.getClass())))
            .httpStatus(httpStatus)
            .title(httpStatus.getReasonPhrase())
            .detail(exception.getMessage())
            .instance(URI.create(request.getRequest().getRequestURI()))
            .buildResponseEntity();
    }
}