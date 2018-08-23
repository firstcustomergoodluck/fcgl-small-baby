package com.fcgl.Listing.Vendors.Amazon.SubmitFeed;
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
import java.util.ArrayList;
import java.util.Arrays;
//FCGL Imports
import com.fcgl.Exceptions.RetryLimitException;
import com.fcgl.Listing.IGenerateFeed;
import com.fcgl.Listing.Response;
import com.fcgl.Listing.Vendors.*;
import com.fcgl.Listing.Vendors.Amazon.AbstractAMZService;
import com.fcgl.Listing.Vendors.Amazon.ErrorHandlerAMZ;
import com.fcgl.Listing.Vendors.Amazon.XML_Files.IXMLGenerator;
import com.fcgl.Listing.Vendors.Amazon.XML_Files.XMLGeneratorAMZ;

//TODO: Need to add a log under every error
//TODO: Need to add a log after a successful API call
//TODO: Need to set up RabbitMQ for message queues.
//TODO: Need to backlog xml file location if there were no errors in XML but there was an error in submission.
//TODO: Look at third parties for XML generators

/**
 * Submits a Feed on Amazon.
 * Workflow:
 *          1. Gets Product Data from Message Queue
 *          2. Generates XML File with data that was returned from Message Queue
 *          3. Calls Amazon's submitFeed API
 */
public class GenerateFeedAMZ extends AbstractAMZService implements IGenerateFeed {

    private final String requestId;    //TODO: Need to move all of these to a config file and make it secret (look up 3rd party encryption)
    private final String MWSAuthToken = "";
    private final String merchantId = "";
    private final String marketplaceId = "";
    private final int[] retryWaitTime = {1000,4000,16000,30000};//This was recommended by Amazon

    /**
     * GenerateFeedAMZ constructor
     * @param requestId: The requestId generated by the CRON Job.
     */
    GenerateFeedAMZ(String requestId) {
        this.requestId = requestId;
    }


    /**
     * Completes the entire workflow. Gets Product Data, Generates XML, Calls AMAZON API.
     * @return Response returned by Amazon Marketplace Web Services
     * @throws InterruptedException: Thread interruption
     */
    public Response<SubmitFeedSuccessAMZ, ErrorHandlerAMZ> generateFeed() throws InterruptedException {
        MarketplaceWebService service = instantiateService();// Instantiates MWS service

        ArrayList<ArrayList<ProductInformation>> productInformation = dequeu();//Gets product data from message queue

        String fileLocation = generateProductXML(productInformation);//Generates an xml file for the product information

        try {
            return submitFeed(fileLocation, service, 0);
        } catch (RetryLimitException e) {
            String message =  e.getMessage();
            int statusCode = 500;
            ErrorHandlerAMZ errorHandlerAMZ = new ErrorHandlerAMZ(message, getRequestId(), statusCode);
            return new Response<>(errorHandlerAMZ);
        }
    }

    //TODO: Need to set up RabbitMQ and probably need to do this in another class
    /**
     * Gets product data from message queue
     * @return the product data
     */
    private ArrayList<ArrayList<ProductInformation>> dequeu() {
        return new ArrayList<>();
    }

    /**
     * Generates an XML file with the product information passed into this method.
     * @param productInformation: The product data returned from the message queue.
     * @return the full path of the location of the XML file that was generated.
     */
    private String generateProductXML(ArrayList<ArrayList<ProductInformation>> productInformation) {
        IXMLGenerator productXML = new XMLGeneratorAMZ();
        return productXML.generateProductXML(productInformation, getRequestId());
    }

    /**
     * Generates the request for submitting a feed
     * Throttling: Request Quota: 15 requests, Restore Rate: 1 request every 2 minutes, Hourly quota: 30 requests
     * Amazon Docs: http://docs.developer.amazonservices.com/en_US/feeds/Feeds_SubmitFeed.html
     * @param fileLocation: the file location of the product feed
     * @param service: A MarketplaceWebService instance
     * @param retryAttempt: The number of times the Amazon API will be called when there is a server error
     * @return Response: A Response is one of SubmitFeedSuccessAMZ or ErrorHandlerAMZ.
     * @throws RetryLimitException: Will be thrown when the retry limit is reached
     * @throws InterruptedException: Thread error
     */
    private Response<SubmitFeedSuccessAMZ, ErrorHandlerAMZ> submitFeed(String fileLocation, MarketplaceWebService service, int retryAttempt) throws RetryLimitException, InterruptedException {
        IdList marketplaces = new IdList(Arrays.asList(marketplaceId));
        SubmitFeedRequest request = new SubmitFeedRequest();
        request.setMerchant(merchantId);
        request.setMWSAuthToken(MWSAuthToken);
        request.setMarketplaceIdList(marketplaces);
        request.setFeedType("_POST_PRODUCT_DATA_");

        try {
            request.setFeedContent(new FileInputStream(fileLocation));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            String message = String.format("The file: %s was not found, try regenerating the file", fileLocation);
            int statusCode = 404;

            return new Response<>( new ErrorHandlerAMZ(message, requestId, statusCode));
        }

        Response<SubmitFeedSuccessAMZ, ErrorHandlerAMZ> invokeSubmitFeedResponse =  invokeSubmitFeed(service, request);

        //Does some retry logic to handle throttling or server errors
        if (invokeSubmitFeedResponse.isError()) {
            ErrorHandlerAMZ errorHandlerAMZ = invokeSubmitFeedResponse.getErrorHandler();
            if (retryAttempt > 4) {
                throw new RetryLimitException("Retry Attempts Exceeded for submitFeed()");
            }

            if (errorHandlerAMZ.getStatusCode() >= 500) {
                Thread.sleep(retryWaitTime[retryAttempt]);
                submitFeed(fileLocation, service, retryAttempt++);
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
     * @return Response: A Response is one of SubmitFeedSuccessAMZ or ErrorHandlerAMZ.
     */
    //TODO: Instead of printing need to log
    private Response<SubmitFeedSuccessAMZ, ErrorHandlerAMZ> invokeSubmitFeed(MarketplaceWebService service, SubmitFeedRequest request) {
        //HashMap<String, Object> responseResult = new HashMap<>();
        SubmitFeedSuccessAMZ submitFeedSuccess = new SubmitFeedSuccessAMZ();

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
                        submitFeedSuccess.setSubmissionId(feedSubmissionInfo.getFeedSubmissionId());
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
                        submitFeedSuccess.setProcessingStatus(feedSubmissionInfo.getFeedSubmissionId());
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
                    submitFeedSuccess.setRequestId(responseMetadata.getRequestId());
                }
            }
            System.out.println(response.getResponseHeaderMetadata());//Has some useful stuff in here that maybe I want??? I should defintly log the timestamp
            System.out.println();
            System.out.println();
            submitFeedSuccess.setTimestamp(response.getResponseHeaderMetadata().getTimestamp());
            submitFeedSuccess.setStatusCode(200);
            return new Response<>(submitFeedSuccess);

        } catch (MarketplaceWebServiceException ex) {
            String message = ex.getMessage();
            int statusCode = ex.getStatusCode();
            String errorCode = ex.getErrorCode();
            String errorType = ex.getErrorType();
            String requestIdAMZ = ex.getRequestId();
            String timestamp = ex.getResponseHeaderMetadata().getTimestamp();
            System.out.println("Caught Exception: " + message);
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
            System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());

            ErrorHandlerAMZ errorHandlerAMZ = new ErrorHandlerAMZ(message, this.getRequestId(), statusCode);
            errorHandlerAMZ.setErrorCode(errorCode);
            errorHandlerAMZ.setErrorType(errorType);
            errorHandlerAMZ.setRequestIdAMZ(requestIdAMZ);
            errorHandlerAMZ.setTimestamp(timestamp);
            return new Response<>(errorHandlerAMZ);
        }
    }

    private String getRequestId() {
        return requestId;
    }
}
