package com.fcgl.Listing;

/**
 * Concrete class for ErrorHandler.
 * Any Error Handler implementation should extend this class
 */
public class ErrorHandler implements IErrorHandler {
    private final String ERROR_MESSAGE = "ErrorHandler has an empty %s";
    private String message;
    private String requestId;
    private int statusCode;

    public ErrorHandler(String message, String requestId, int statusCode) {

        isBadConstructor(message, requestId);
        this.message = message;
        this.statusCode = statusCode;
        this.requestId = requestId;
    }


    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getRequestId() {
        return requestId;
    }

    private void isBadConstructor(String message, String requestId) {
        isBadString(message, "Message");
        isBadString(requestId, "Request ID");
    }

    private void isBadString(String value, String variable) {
        if (value == null || value.length() == 0) {
            throw new NullPointerException(String.format(ERROR_MESSAGE, variable));
        }
    }
}
