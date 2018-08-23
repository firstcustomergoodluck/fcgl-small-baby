package com.fcgl.Listing;


public interface ISuccessHandler {
    String getRequestId();
    int getStatusCode();
    void setStatusCode(int status);
}
