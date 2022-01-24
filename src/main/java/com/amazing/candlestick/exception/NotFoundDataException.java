package com.amazing.candlestick.exception;

public class NotFoundDataException extends Exception {
    public NotFoundDataException() {
        super();
    }

    public NotFoundDataException(String message) {
        super(message);
    }

    public NotFoundDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundDataException(Throwable cause) {
        super(cause);
    }

    protected NotFoundDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
