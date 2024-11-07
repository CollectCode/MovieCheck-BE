package com.movie.moviecheck.exception;

public class CustomerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CustomerException(String message) {
        super(message);
    }
}
