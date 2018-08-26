package com.fcgl.Listing.Response;

/**
 * Basic format for what should be returned to an endpoint
 *      isError: boolean that indicates if there was an error or not
 *      statusCode: the status code of the response
 *      message: a descriptive message about the response
 */
public interface IResponse {

    boolean isError();
    int getStatusCode();
    String getMessage();
    String getRequestId();
}
