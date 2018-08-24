package com.fcgl.Listing.Response;

import com.fcgl.Listing.Response.ErrorHandler.IErrorHandler;
import com.fcgl.Listing.Response.SuccessHandler.ISuccessHandler;

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
