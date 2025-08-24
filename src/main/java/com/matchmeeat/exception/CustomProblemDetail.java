package com.matchmeeat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Map;

public class CustomProblemDetail extends ProblemDetail {

    private CustomProblemDetail() {
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public static Builder builder() {
        return new Builder();
    }

    @Getter
    public static class Builder {

        private HttpStatus httpStatus;
        private String detail;
        private String title;
        private URI instance;
        private URI type;
        private Map<String, String> errors;
        private Map<String, Object> properties;

        private Builder() {
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder instance(URI instance) {
            this.instance = instance;
            return this;
        }

        public Builder type(URI type) {
            this.type = type;
            return this;
        }

        public Builder errors(Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public Builder properties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public ProblemDetail build() {
            ProblemDetail problemDetail = ProblemDetail.forStatus(this.httpStatus);
            if (this.detail != null) problemDetail.setDetail(this.detail);
            if (this.title != null) problemDetail.setDetail(this.title);
            if (this.instance != null) problemDetail.setInstance(this.instance);
            if (this.type != null) problemDetail.setType(this.type);
            if (this.properties != null) problemDetail.setProperties(this.properties);
            if (this.errors != null) problemDetail.setProperty("errors", this.errors);
            return problemDetail;
        }

        public ResponseEntity<ProblemDetail> buildResponseEntity() {
            ProblemDetail problemDetail = build();
            return ResponseEntity.ok(problemDetail);
        }
    }
}
