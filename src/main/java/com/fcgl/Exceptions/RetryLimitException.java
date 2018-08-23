package com.fcgl.Exceptions;

/**
 * Exception is thrown when the retry limit for an API has been reached
 */
public class RetryLimitException extends Exception {

    public RetryLimitException(String message) {
        super(message);
    }
}
