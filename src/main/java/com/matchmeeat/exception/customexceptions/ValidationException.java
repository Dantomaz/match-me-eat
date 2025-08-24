package com.matchmeeat.exception.customexceptions;

import com.matchmeeat.exception.CustomProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.util.Map;

public class ValidationException extends ErrorResponseException {

    public ValidationException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public ValidationException(HttpStatus httpStatus, String title, String detail) {
        super(httpStatus, CustomProblemDetail.builder().httpStatus(httpStatus).title(title).detail(detail).build(), null);
    }

    public ValidationException(HttpStatus httpStatus, String title, String detail, Map<String, String> errors) {
        super(httpStatus, CustomProblemDetail.builder().httpStatus(httpStatus).title(title).detail(detail).errors(errors).build(), null);
    }

    public ValidationException(HttpStatus httpStatus, Map<String, String> errors) {
        super(httpStatus, CustomProblemDetail.builder().httpStatus(httpStatus).detail("Request validation failed").errors(errors).build(), null);
    }

    public ValidationException(HttpStatus httpStatus, Map<String, String> errors, Throwable cause) {
        super(httpStatus, CustomProblemDetail.builder().httpStatus(httpStatus).detail("Request validation failed").errors(errors).build(), cause);
    }
}
