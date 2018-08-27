package com.fcgl.Listing.Response;

/**
 * Response that each class should return.
 */
public class Response implements IResponse {
    private final String ERROR_MESSAGE = "ErrorHandler has an empty %s";
    private boolean error;
    private int statusCode;
    private String message;
    private String requestId;

    /**
     *
     * @param error: Indicates if there was an error
     * @param message: Descriptive message of response
     * @param requestId: requestId
     * @param statusCode: Status code
     */
    public Response(boolean error, int statusCode, String requestId, String message) {
        validateConstructor(message, requestId);
        this.error = error;
        this.message = message;
        this.statusCode = statusCode;
        this.requestId = requestId;
    }

    public boolean isError() {
        return error;
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

    /**
     * Validates that a constructor has valid values.
     * @param message: message passed into the constructor
     * @param requestId requestId passed into the constructor
     */
    private void validateConstructor(String message, String requestId) {
        validateString(message, "Message");
        validateString(requestId, "Request ID");
    }

    /**
     * Validates if a String is correctly passed in.
     * @param value: the String being evaluated
     * @param variable: the name of the variable. Used for error messaging.
     */
    private void validateString(String value, String variable) {
        if (value == null || value.length() == 0) {
            throw new NullPointerException(String.format(ERROR_MESSAGE, variable));
        }
    }
}