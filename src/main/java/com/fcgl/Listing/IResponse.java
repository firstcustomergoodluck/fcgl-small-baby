package com.fcgl.Listing;

/**
 * Basic format for what should be returned to the endpoint
 *      IErrorHandler: An IErrorHandler Object
 *                      OR
 *      ISuccessHandler: Am ISuccessHandler
 *      isError: 
 */
public interface IResponse {

    IErrorHandler getErrorHandler();
    ISuccessHandler getSuccessHandler();
    boolean isError();
}
