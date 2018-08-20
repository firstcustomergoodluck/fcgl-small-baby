package com.fcgl.Listing.Vendors.Amazon;
//Amazon Imports
import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.IdList;
import com.amazonaws.mws.model.SubmitFeedRequest;
import com.amazonaws.mws.model.SubmitFeedResponse;
import com.amazonaws.mws.model.SubmitFeedResult;
import com.amazonaws.mws.model.FeedSubmissionInfo;
import com.amazonaws.mws.model.ResponseMetadata;
//Java Imports
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
//FCGL Imports
import com.fcgl.Errors.RetryLimitException;
import com.fcgl.Listing.IGenerateFeed;

//TODO: Need to add a log under every error
//TODO: Need to add a log after a successful API call
//TODO: Need to set up RabbitMQ for message queues.
//TODO: Need to backlog xml file location if there were no errors in XML but there was an error in submission.
//TODO: Look at third parties for XML generators

/**
 * Submits a Feed on Amazon
 */
public class GenerateFeedAMZ extends AbstractAMZService implements IGenerateFeed {

    //TODO: Need to move all of these to a config file and make it secret (look up 3rd party encryption)
    private final String MWSAuthToken = "";
    private final String merchantId = "";
    private final String marketplaceId = "";
    private final int[] retryWaitTime = {1000,4000,16000,30000};

    /**
     * Generates a feed
     * @return Response returned by Amazon Marketplace Web Services
     */
    public HashMap<String, Object> generateFeed() throws InterruptedException {
        MarketplaceWebService service = instantiateService();

        HashMap<String, Object> productInformation = dequeu();//Gets product data from message queue

        if ((boolean)productInformation.get("error")) {
            return productInformation;
        }

        String fileLocation = generateProductXML(productInformation);//Generates an xml file for the product information

        try {
            HashMap<String, Object> submitFeedResponse = submitFeed(fileLocation, service, 0);
            if ((boolean) submitFeedResponse.get("error")) {
                return submitFeedResponse;
            }
            return submitFeedResponse;

        } catch (RetryLimitException e) {
            HashMap<String, Object> errorHandler = new HashMap<>();
            errorHandler.put("error", true);
            errorHandler.put("message", e.getMessage());
            errorHandler.put("status", 500);
            return errorHandler;
        }
    }

    //TODO: Need to set up RabbitMQ and probably need to do this in another class
    /**
     * Gets product data from message queue
     * @return the product data
     */
    private HashMap<String, Object> dequeu() {
        return new HashMap<String, Object>();
    }

    /**
     * Generates an XML file with the product information passed into this method.
     * @param productInformation: The product data returned from the message queue
     * @return the full path of the location of the xml file that was generated.
     */
    private String generateProductXML(HashMap<String, Object> productInformation) {
        //TODO: Look for some good third parties that can make big XML files efficiently.
        return "";
    }

    /**
     * Generates the request for submitting a feed
     * Throttling: Request Quota: 15 requests, Restore Rate: 1 request every 2 minutes, Hourly quota: 30 requests
     * Amazon Docs: http://docs.developer.amazonservices.com/en_US/feeds/Feeds_SubmitFeed.html
     * @param fileLocation: the file location of the product feed
     * @return the data obtained from invokeSubmitFeed
     * @error returns an error boolean, status code, and descriptive message
     */
    private HashMap<String, Object> submitFeed(String fileLocation, MarketplaceWebService service, int retryAttempt) throws RetryLimitException, InterruptedException {
        IdList marketplaces = new IdList(Arrays.asList(marketplaceId));
        SubmitFeedRequest request = new SubmitFeedRequest();
        request.setMerchant(merchantId);
        request.setMWSAuthToken(MWSAuthToken);
        request.setMarketplaceIdList(marketplaces);
        request.setFeedType("_POST_PRODUCT_DATA_");

        try {
            request.setFeedContent(new FileInputStream(fileLocation));
        } catch (FileNotFoundException e) {
            HashMap<String, Object> errorResponse = new HashMap<>();
            e.printStackTrace();
            errorResponse.put("error", true);
            errorResponse.put("status", 404);
            errorResponse.put("message", String.format("The file: %s was not found, try regenerating the file", fileLocation));
            return errorResponse;
        }

        HashMap<String, Object> invokeSubmitFeedResponse =  invokeSubmitFeed(service, request);

        //Does some retry logic to handle throttling or server errors
        if ((boolean)invokeSubmitFeedResponse.get("error")) {
            if (retryAttempt > 4) {
                throw new RetryLimitException("Retry Attempts Exceeded for submitFeed()");
            }

            if ((int) invokeSubmitFeedResponse.get("status") >= 500) {
                Thread.sleep(retryWaitTime[retryAttempt]);
                submitFeed(fileLocation, service, retryAttempt++);
            } else {
                return invokeSubmitFeedResponse;
            }
        }

        return invokeSubmitFeedResponse;
    }


    /**
     * Submit Feed request sample Uploads a file for processing together with
     * the necessary metadata to process the file, such as which type of feed it
     * is. PurgeAndReplace if true means that your existing e.g. inventory is
     * wiped out and replace with the contents of this feed - use with caution
     * (the default is false).
     * @param service: instance of MarketplaceWebService service
     * @param request: a SubmitFeedRequest
     */
    //TODO: Instead of printing need to log
    private static HashMap<String, Object> invokeSubmitFeed(MarketplaceWebService service, SubmitFeedRequest request) {
        HashMap<String, Object> responseResult = new HashMap<>();

        try {
            SubmitFeedResponse response = service.submitFeedFromFile(request);
            System.out.println("SubmitFeed Header");
            System.out.println("SubmitFeed Action Response");
            System.out
                    .println("=============================================================================");
            System.out.println();

            System.out.print("    SubmitFeedResponse");
            System.out.println();
            if (response.isSetSubmitFeedResult()) {
                System.out.print("        SubmitFeedResult");
                System.out.println();
                SubmitFeedResult submitFeedResult = response
                        .getSubmitFeedResult();
                if (submitFeedResult.isSetFeedSubmissionInfo()) {
                    System.out.print("            FeedSubmissionInfo");
                    System.out.println();
                    FeedSubmissionInfo feedSubmissionInfo = submitFeedResult
                            .getFeedSubmissionInfo();
                    if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                        System.out.print("                FeedSubmissionId");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedSubmissionId());
                        System.out.println();
                        responseResult.put("submissionId", feedSubmissionInfo.getFeedSubmissionId());
                    }
                    if (feedSubmissionInfo.isSetFeedType()) {
                        System.out.print("                FeedType");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedType());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetSubmittedDate()) {
                        System.out.print("                SubmittedDate");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getSubmittedDate());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                        System.out
                                .print("                FeedProcessingStatus");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedProcessingStatus());
                        System.out.println();
                        responseResult.put("processingStatus", feedSubmissionInfo.getFeedSubmissionId());
                    }
                    if (feedSubmissionInfo.isSetStartedProcessingDate()) {
                        System.out
                                .print("                StartedProcessingDate");
                        System.out.println();
                        System.out
                                .print("                    "
                                        + feedSubmissionInfo
                                        .getStartedProcessingDate());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetCompletedProcessingDate()) {
                        System.out
                                .print("                CompletedProcessingDate");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo
                                .getCompletedProcessingDate());
                        System.out.println();
                    }
                }
            }
            if (response.isSetResponseMetadata()) {
                System.out.print("        ResponseMetadata");
                System.out.println();
                ResponseMetadata responseMetadata = response
                        .getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    System.out.print("            RequestId");
                    System.out.println();
                    System.out.print("                "
                            + responseMetadata.getRequestId());
                    System.out.println();
                    responseResult.put("RequestId", responseMetadata.getRequestId());
                }
            }
            System.out.println(response.getResponseHeaderMetadata());//Has some useful stuff in here that maybe I want??? I should defintly log the timestamp
            System.out.println();
            System.out.println();
            responseResult.put("error", false);
            responseResult.put("status", 200);

        } catch (MarketplaceWebServiceException ex) {
            System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
            System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
            responseResult.put("error", true);
            responseResult.put("message", ex.getMessage());
            responseResult.put("status", ex.getStatusCode());
            responseResult.put("errorCode", ex.getErrorCode());
            responseResult.put("errorType", ex.getErrorType());
            responseResult.put("requestId", ex.getRequestId());
        }
        return responseResult;
    }
}
