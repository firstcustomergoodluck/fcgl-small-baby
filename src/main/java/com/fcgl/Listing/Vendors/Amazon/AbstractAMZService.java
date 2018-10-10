package com.fcgl.Listing.Vendors.Amazon;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;

/**
 * Abstract class for Amazon Listing API
 */
public abstract class AbstractAMZService {

  /**
   * Instantiates the connection with Amazon Marketplace Web Services
   *
   * @return Http Client Implementation of MWS
   */
  public MarketplaceWebService instantiateService() {
    final String SecretKey = "";
    final String appName = "fcgl";
    final String appVersion = "1.0";
    final String AWSAccessKeyId = "";

    MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
    config.setServiceURL("https://mws.amazonservices.com/"); // MWS US endpoint

    //Instantiate Http Client Implementation of Marketplace Web Service
    return new MarketplaceWebServiceClient(
        AWSAccessKeyId, SecretKey, appName, appVersion, config);
  }


}
