package com.fcgl.Listing.Response.SuccessHandler;


public interface ISuccessHandler {
    String getRequestId();
    int getStatusCode();
    void setStatusCode(int status);
}
