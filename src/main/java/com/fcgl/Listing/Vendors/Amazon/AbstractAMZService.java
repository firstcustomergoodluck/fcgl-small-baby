package com.fcgl.Listing.Vendors.Amazon;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.fcgl.Listing.Response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract class for Amazon Listing API
 */
public abstract class AbstractAMZService {

  private static final AmazonConfig amazonConfig = new AmazonConfig();

  /**
   * Instantiates the connection with Amazon Marketplace Web Services
   *
   * @return Http Client Implementation of MWS
   */
  public MarketplaceWebService instantiateService() {
    final String SecretKey = amazonConfig.getSecretKey();
    final String appName = amazonConfig.getAppName();
    final String appVersion = amazonConfig.getAppVersion();
    final String AWSAccessKeyId = amazonConfig.getAWSAccessKeyId();

    MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
    config.setServiceURL("https://mws.amazonservices.com/"); // MWS US endpoint

    //Instantiate Http Client Implementation of Marketplace Web Service
    return new MarketplaceWebServiceClient(
        AWSAccessKeyId, SecretKey, appName, appVersion, config);
  }

  public static AmazonConfig getAmazonConfig() {
    return amazonConfig;
  }

  /**
   * All Amazon Exceptions have the same set up, log and return an error Response object
   * @param ex: the exception being handled
   * @param className: the class name where the exception is being thrown from
   * @param requestId: the request id passed in
   * @return Response (error = true)
   */
  public Response handleErrorResponse(MarketplaceWebServiceException ex, String className, String requestId) {
    StringBuilder builder = new StringBuilder();
    String message = ex.getMessage();
    int statusCode = ex.getStatusCode();
    String amzErrorCode = ex.getErrorCode();
    String amzErrorType = ex.getErrorType();
    String amzRequestId = ex.getRequestId();
    String timestamp = ex.getResponseHeaderMetadata().getTimestamp();
    builder.append("Caught Exception: ");
    builder.append(message);
    builder.append("\nResponse Status Code: ");
    builder.append(ex.getStatusCode());
    builder.append("\nError Code: ");
    builder.append(amzErrorCode);
    builder.append("\nError Type: ");
    builder.append(amzErrorType);
    builder.append("\nRequest ID: ");
    builder.append(amzRequestId);
    builder.append("\nTimestamp: ");
    builder.append(timestamp);
    builder.append("\nXML: ");
    builder.append(ex.getXML());
    builder.append("\nResponseHeaderMetadata: ");
    builder.append(ex.getResponseHeaderMetadata());
    builder.append("\n");
    Logger logger = LogManager.getLogger(className);
    logger.error(builder.toString());
    return new Response(true, statusCode, requestId, message);
  }
}
