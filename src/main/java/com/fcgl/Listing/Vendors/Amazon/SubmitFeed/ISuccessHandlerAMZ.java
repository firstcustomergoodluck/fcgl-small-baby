package com.fcgl.Listing.Vendors.Amazon.SubmitFeed;

public interface ISuccessHandlerAMZ{

    String getSubmissionId();

    void setSubmissionId(String submissionId);

    String getProcessingStatus();

    void setProcessingStatus(String processingStatus);

    int getStatusCode();

    void setStatusCode(int status);

    String getTimestamp();

    void setTimestamp(String timestamp);

    String getRequestId();

    void setRequestId(String requestId);
}
