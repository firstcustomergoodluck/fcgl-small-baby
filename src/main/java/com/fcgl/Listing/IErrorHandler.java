package com.fcgl.Listing;

/**
 * Basic format for what should be returned when there is an Error.
 *      message: A descriptive message of the error
 *      status code: A status code that describes the type of error.
 *      requestId: The requestId sent to the endpoint
 */
public interface IErrorHandler {

    /**
     * Gets the ErrorHandler message
     * @return message
     */
    String getMessage();

    /**
     * Gets the ErrorHandler status code
     * @return statusCode
     */
    int getStatusCode();

    /**
     * Gets the requestId
     * @return requestId
     */
    String getRequestId();

}
