package com.fcgl.Listing.Vendors.Amazon.SubmitFeed;

import com.fcgl.Listing.Response.SuccessHandler.ISuccessHandler;

//TODO: Would it be good to save this in our database for our record?

/**
 * When a successful call is made to the submitFeed AMAZON API then this is what it should return
 */
public class SubmitFeedSuccessAMZ implements ISuccessHandler {
    private String submissionId;
    private String processingStatus;
    private int statusCode;
    private String timestamp;
    private String requestId;

    public SubmitFeedSuccessAMZ() {

    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int status) {
        this.statusCode = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
