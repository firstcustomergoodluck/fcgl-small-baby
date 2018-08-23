package com.fcgl.Listing;

/**
 * Basic format for what should be returned to an endpoint
 *      IErrorHandler: An IErrorHandler Object
 *                      OR
 *      ISuccessHandler: An ISuccessHandler
 *      isError:
 */
public interface IResponse {

    IErrorHandler getErrorHandler();
    ISuccessHandler getSuccessHandler();
    boolean isError();
}
