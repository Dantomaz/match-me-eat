package com.matchmeeat.exception.customexceptions;

public class EmailNotSentException extends RuntimeException {

    public EmailNotSentException(String message) {
        super(message);
    }
}
