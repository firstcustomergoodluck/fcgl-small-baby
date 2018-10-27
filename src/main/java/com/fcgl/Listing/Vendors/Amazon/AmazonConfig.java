package com.fcgl.Listing.Vendors.Amazon;

public class AmazonConfig {

  //TODO: Should populate from database...
  private final String MWSAuthToken = "";
  private final String merchantId = "";
  private final String marketplaceId = "";
  private final String SecretKey = "";
  private final String appName = "fcgl";
  private final String appVersion = "1.0";
  private final String AWSAccessKeyId = "";

  public String getAppName() {
    return appName;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public String getAWSAccessKeyId() {
    return AWSAccessKeyId;
  }

  public String getMarketplaceId() {
    return marketplaceId;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public String getMWSAuthToken() {
    return MWSAuthToken;
  }

  public String getSecretKey() {
    return SecretKey;
  }
}
