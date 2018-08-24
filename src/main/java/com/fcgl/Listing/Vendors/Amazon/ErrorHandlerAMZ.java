package com.fcgl.Listing.Vendors.Amazon;

import com.fcgl.Listing.Response.ErrorHandler.ErrorHandler;

public class ErrorHandlerAMZ extends ErrorHandler {
    private final String ERROR_MESSAGE = "ErrorHandlerAMZ has an empty %s";
    private String errorCode;
    private String errorType;
    private String requestIdAMZ;
    private String timestamp;

    public ErrorHandlerAMZ(String message, String requestId, int statusCode) {
        super(message, requestId, statusCode);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getRequestIdAMZ() {
        return requestIdAMZ;
    }

    public void setRequestIdAMZ(String requestId) {
        this.requestIdAMZ = requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
