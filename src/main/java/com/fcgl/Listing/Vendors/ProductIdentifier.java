package com.fcgl.Listing.Vendors;

public class ProductIdentifier {

  ProductIdentifierType productIdentifierType;
  String value;
  public ProductIdentifier(ProductIdentifierType productIdentifierType, String value) {
    this.productIdentifierType = productIdentifierType;
    this.value = value;
  }

  public ProductIdentifierType getProductIdentifierType() {
    return productIdentifierType;
  }

  public String getValue() {
    return value;
  }

}
