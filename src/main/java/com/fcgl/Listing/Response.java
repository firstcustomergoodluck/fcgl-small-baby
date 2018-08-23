package com.fcgl.Listing;

/**
 * Generic Response that each class should return.
 * @param <SUCCESSHANDLER> An Object that implements ISuccessHandler
 * @param <ERRORHANDLER> An Object that implements IErrorHandler
 */
public class Response<SUCCESSHANDLER extends ISuccessHandler, ERRORHANDLER extends IErrorHandler> implements IResponse{
    private boolean error;
    private ERRORHANDLER errorHandler;
    private SUCCESSHANDLER successHandler;

    public Response(SUCCESSHANDLER successHandler) {
        this.successHandler = successHandler;
        this.error = false;
    }

    public Response(ERRORHANDLER errorHandler) {
        this.errorHandler = errorHandler;
        this.error = true;
    }

    public boolean isError() {
        return error;
    }

    public ERRORHANDLER getErrorHandler() {
        if (this.error) {
            return errorHandler;
        }
        throw new RuntimeException("Cannot get Error Handler if Response was instantiated with an Success Handler");
    }

    public SUCCESSHANDLER getSuccessHandler() {
        if (!this.error)
            return successHandler;
        throw new RuntimeException("Cannot get Success Handler if Response was instantiated with an Error Handler");
    }

}


//package com.fcgl.Listing.Vendors;
//
//import com.fcgl.Listing.IErrorHandler;
//
//public class Response {
//    private boolean error;
//    private IErrorHandler errorHandler;
//    private ISuccessHandler successHandler;
//
//    public Response(ISuccessHandler successHandler) {
//        this.successHandler = successHandler;
//        this.error = false;
//    }
//
//    public Response(IErrorHandler errorHandler) {
//        this.errorHandler = errorHandler;
//        this.error = true;
//    }
//
//    public boolean isError() {
//        return error;
//    }
//
//    public IErrorHandler getErrorHandler() {
//        if (this.error) {
//            return errorHandler;
//        }
//        throw new RuntimeException("Cannot get Error Handler if Response was instantiated with an Success Handler");
//    }
//
//    public ISuccessHandler getSuccessHandler() {
//        if (!this.error)
//            return successHandler;
//        throw new RuntimeException("Cannot get Success Handler if Response was instantiated with an Error Handler");
//    }
//
//}
